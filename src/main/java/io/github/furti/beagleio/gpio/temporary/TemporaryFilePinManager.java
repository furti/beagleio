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
package io.github.furti.beagleio.gpio.temporary;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.nio.file.attribute.PosixFilePermission;
import java.util.HashSet;
import java.util.Set;

import io.github.furti.beagleio.BeagleIOException;
import io.github.furti.beagleio.Direction;
import io.github.furti.beagleio.Pin;
import io.github.furti.beagleio.PinValue;
import io.github.furti.beagleio.gpio.AbstractPinManager;
import io.github.furti.beagleio.gpio.util.FileUtils;

/**
 * @author Daniel
 *
 */
public class TemporaryFilePinManager extends AbstractPinManager
{
  private Path pinDirectory;
  private Path activeLowFile;
  private Path directionFile;
  private Path edgeFile;
  private Path powerFile;
  private Path ueventFile;
  private Path valueFile;

  /**
   * @param pin
   * @param baseDirectory
   */
  public TemporaryFilePinManager(Pin pin, Path baseDirectory)
  {
    addOperation(this::initPinDirectory, baseDirectory.resolve(pin.toString()));
  }

  /**
   * Creates the directory for the pin and all files that are needed for the gpio system to work.
   * 
   * @param pinDirectory the directory for the pin
   */
  protected void initPinDirectory(Path pinDirectory)
  {
    this.pinDirectory = pinDirectory;

    try
    {

      if (!Files.exists(pinDirectory))
      {
        Files.createDirectory(pinDirectory);
        createPermissions(pinDirectory);
      }

      activeLowFile = createIfNotExists(pinDirectory, "active_low");
      directionFile = createIfNotExists(pinDirectory, "direction");
      edgeFile = createIfNotExists(pinDirectory, "edge");
      powerFile = createIfNotExists(pinDirectory, "power");
      ueventFile = createIfNotExists(pinDirectory, "uevent");
      valueFile = createIfNotExists(pinDirectory, "value");

      // Initialize the value with LOW
      writeToFile(valueFile, "0");
    } catch (IOException e)
    {
      throw new BeagleIOException("Error initializing Temporary Pin Directory " + pinDirectory, e);
    }
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
      FileUtils.deleteDirectory(pinDirectory);
    } catch (IOException e)
    {
      throw new BeagleIOException("An error occured while deleting the temporary Pin Directory "
          + pinDirectory, e);
    }
  }

  /**
   * @param path to write to
   * @param value to write to the file. The toString method is used to obtain the actual value to
   *        write to the file.
   */
  private void writeToFile(Path path, Object value)
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
  private String readFromFile(Path path)
  {
    try (BufferedReader reader = Files.newBufferedReader(path, Charset.defaultCharset()))
    {
      return reader.readLine();
    } catch (IOException e)
    {
      throw new BeagleIOException("Error reading value from file " + path, e);
    }
  }

  private Path createIfNotExists(Path directory, String fileName) throws IOException
  {
    Path file = directory.resolve(fileName);

    if (!Files.exists(file))
    {
      Files.createFile(file);

      createPermissions(file);
    }

    return file;
  }

  private void createPermissions(Path path) throws IOException
  {
    if (FileUtils.IS_POSIX)
    {
      Set<PosixFilePermission> permissions = new HashSet<>(3);
      permissions.add(PosixFilePermission.OWNER_READ);
      permissions.add(PosixFilePermission.OWNER_WRITE);
      permissions.add(PosixFilePermission.OWNER_EXECUTE);
      Files.setPosixFilePermissions(path, permissions);
    }
  }
}
