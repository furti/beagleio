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

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.WatchService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import io.github.furti.beagleio.BeagleIOException;
import io.github.furti.beagleio.Pin;
import io.github.furti.beagleio.gpio.AbstractBeagle;
import io.github.furti.beagleio.gpio.PinManager;
import io.github.furti.beagleio.gpio.util.FileUtils;

/**
 * A Beagle implementation that creates a tmp directory on the filesystem with the same layout as
 * the GPIO Filesystem on the BeagleBone Black. This can be used on development Machines without a
 * GPIO System to thest the functionality of the application.
 * 
 * @author Daniel
 *
 */
public class TemporaryFilesystemBeagle extends AbstractBeagle
{
  private Path baseDirectory;
  private ScheduledExecutorService executor;
  private WatchService watcher;


  /**
   * @throws IOException if an exception occurs creating the tmp directory.
   * 
   */
  public TemporaryFilesystemBeagle() throws IOException
  {
    this.setupTmpDirectory();
    executor = Executors.newSingleThreadScheduledExecutor();
    watcher = FileSystems.getDefault().newWatchService();
  }

  private void setupTmpDirectory() throws IOException
  {
    baseDirectory = Paths.get(System.getProperty("java.io.tmpdir"), "beagleio");
    Files.createDirectories(baseDirectory);
  }

  /*
   * (non-Javadoc)
   * 
   * @see io.github.furti.beagleio.Beagle#release()
   */
  @Override
  public void doRelease()
  {
    try
    {
      executor.shutdownNow();
      watcher.close();
      FileUtils.deleteDirectory(baseDirectory);
    } catch (IOException e)
    {
      throw new BeagleIOException("Error deleting tmp directory", e);
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * io.github.furti.beagleio.gpio.AbstractBeagle#createPinManager(io.github.furti.beagleio.Pin)
   */
  @Override
  protected PinManager createPinManager(Pin pin)
  {
    return new TemporaryFilePinManager(pin, baseDirectory, executor, watcher);
  }
}
