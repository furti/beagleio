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

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.WatchService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import io.github.furti.beagleio.BeagleIOException;
import io.github.furti.beagleio.gpio.AbstractBeagle;

/**
 * Base class for beagles that need to access the filesystem for interactions with the GPIO system.
 * 
 * @author Daniel
 *
 */
public abstract class FileSystemBeagle extends AbstractBeagle
{
  private Path baseDirectory;
  private ScheduledExecutorService executor;
  private WatchService watcher;

  /**
   * @throws IOException if an exception occurs creating the tmp directory.
   */
  public FileSystemBeagle() throws IOException
  {
    baseDirectory = initBaseDirectory();
    executor = Executors.newSingleThreadScheduledExecutor();
    watcher = FileSystems.getDefault().newWatchService();
  }

  public Path getBaseDirectory()
  {
    return baseDirectory;
  }

  public ScheduledExecutorService getExecutor()
  {
    return executor;
  }


  public WatchService getWatcher()
  {
    return watcher;
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
    } catch (IOException e)
    {
      throw new BeagleIOException("Error deleting tmp directory", e);
    }
  }


  /**
   * @return the Path to the base directory of the GPIO System
   * @throws IOException when an exception occurs initializing the directory
   */
  protected abstract Path initBaseDirectory() throws IOException;

}
