/**
 * Copyright 2015 Daniel Furtlehner
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package io.github.furti.beagleio.gpio.file;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import io.github.furti.beagleio.BeagleIOException;
import io.github.furti.beagleio.Direction;
import io.github.furti.beagleio.Pin;
import io.github.furti.beagleio.PinValue;
import io.github.furti.beagleio.PollValue;
import io.github.furti.beagleio.gpio.AbstractPinManager;
import io.github.furti.beagleio.gpio.DefaultPollValue;

/**
 * @author Daniel
 *
 */
public abstract class FileSystemPinManager extends AbstractPinManager
{
  public static final String VALUE_FILE = "value";
  public static final String ACTIVE_LOW_FILE = "active_low";
  public static final String DIRECTION_FILE = "direction";
  public static final String EDGE_FILE = "edge";
  public static final String POWER_FILE = "power";
  public static final String UEVENT_FILE = "uevent";

  private ScheduledExecutorService executor;
  private WatchService watcher;
  private Pin pin;
  private Path pinDirectory;
  private Path activeLowFile;
  private Path directionFile;
  private Path edgeFile;
  private Path powerFile;
  private Path ueventFile;
  private Path valueFile;
  private WatchKey watchKey;
  private DefaultPollValue pollValue;
  private ScheduledFuture<?> pollFuture;

  /**
   * @param pin
   * @param baseDirectory
   * @param watcher
   * @param executor
   */
  public FileSystemPinManager(Pin pin, Path baseDirectory, ScheduledExecutorService executor,
      WatchService watcher)
  {
    this.watcher = watcher;
    this.executor = executor;
    this.pin = pin;
    this.pinDirectory = this.initialize(pin, baseDirectory);


    this.setupFiles();
  }

  /*
   * (non-Javadoc)
   * 
   * @see io.github.furti.beagleio.gpio.AbstractPinManager#doSetDirection(io.github.furti.beagleio.
   * Direction)
   */
  @Override
  protected void doSetDirection(Direction direction)
  {
    writeToFile(directionFile, direction.getValue());
  }

  /*
   * (non-Javadoc)
   * 
   * @see io.github.furti.beagleio.gpio.AbstractPinManager#doSetActiveLow(boolean)
   */
  @Override
  protected void doSetActiveLow(boolean activeLow)
  {
    writeToFile(activeLowFile, activeLow ? Integer.valueOf(1) : Integer.valueOf(0));
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * io.github.furti.beagleio.gpio.AbstractPinManager#doSetValue(io.github.furti.beagleio.PinValue)
   */
  @Override
  protected void doSetValue(PinValue value)
  {
    writeToFile(valueFile, value.getValue());
  }

  @Override
  public PinValue getValue()
  {
    return PinValue.forValue(readFromFile(valueFile));
  }

  @Override
  public PollValue poll()
  {
    try
    {
      watchKey = pinDirectory.register(watcher, StandardWatchEventKinds.ENTRY_MODIFY);
      pollValue = new DefaultPollValue(getValue());
      pollFuture = executor.scheduleAtFixedRate(this::updatePollValue, 10, 10,
          TimeUnit.MILLISECONDS);

      return pollValue;
    } catch (IOException e)
    {
      throw new BeagleIOException("Error polling pin", e);
    }
  }

  private void updatePollValue()
  {
    List<WatchEvent<?>> pollEvents = watchKey.pollEvents();

    for (WatchEvent<?> event : pollEvents)
    {
      Path path = (Path) event.context();

      /*
       * If a change was detected in the value file we have to read the actual value and store it in
       * the pollValue
       */
      if ("value".equals(path.toString()))
      {
        pollValue.setValue(getValue());
        break;
      }
    }


    watchKey.reset();
  }

  /*
   * (non-Javadoc)
   * 
   * @see io.github.furti.beagleio.gpio.AbstractPinManager#doRelease()
   */
  @Override
  protected void doRelease()
  {
    try
    {
      if (pollValue != null)
      {
        watchKey.cancel();
        pollFuture.cancel(true);
      }

      releaseFileSystemPin(pin, pinDirectory);
    } catch (IOException e)
    {
      throw new BeagleIOException("An error occured while releasing the Pin " + pin, e);
    }
  }

  /**
   * @param path to write to
   * @param value to write to the file. The toString method is used to obtain the actual value to
   *        write to the file.
   */
  protected void writeToFile(Path path, Object value)
  {
    try (BufferedWriter writer = Files.newBufferedWriter(path, Charset.defaultCharset(),
        StandardOpenOption.WRITE))
    {
      writer.write(value.toString());
    } catch (IOException e)
    {
      throw new BeagleIOException("Error writing value " + value + " to file " + path, e);
    }
  }

  /**
   * @param path The path to read from
   * @return The content of the file
   */
  protected String readFromFile(Path path)
  {
    try (BufferedReader reader = Files.newBufferedReader(path, Charset.defaultCharset()))
    {
      return reader.readLine();
    } catch (IOException e)
    {
      throw new BeagleIOException("Error reading value from file " + path, e);
    }
  }

  /**
   * Let Implementations do custom release work.
   */
  protected abstract void releaseFileSystemPin(Pin pin, Path pinDirectory) throws IOException;

  /**
   * @param pin the Pin to initialize
   * @param baseDirectory the basedirectory where all pin directories are located
   * @return The directory for the Pin
   */
  protected abstract Path initialize(Pin pin, Path baseDirectory);

  /**
   * Initialize the Paths to all files.
   */
  private void setupFiles()
  {
    this.activeLowFile = this.pinDirectory.resolve(ACTIVE_LOW_FILE);
    this.directionFile = this.pinDirectory.resolve(DIRECTION_FILE);
    this.edgeFile = this.pinDirectory.resolve(EDGE_FILE);
    this.powerFile = this.pinDirectory.resolve(POWER_FILE);
    this.ueventFile = this.pinDirectory.resolve(UEVENT_FILE);
    this.valueFile = this.pinDirectory.resolve(VALUE_FILE);
  }


}
