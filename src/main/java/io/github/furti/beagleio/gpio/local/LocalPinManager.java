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
package io.github.furti.beagleio.gpio.local;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.WatchService;
import java.util.concurrent.ScheduledExecutorService;

import io.github.furti.beagleio.Pin;
import io.github.furti.beagleio.gpio.file.FileSystemPinManager;

/**
 * @author Daniel
 *
 */
public class LocalPinManager extends FileSystemPinManager
{
  private static final String EXPORT_FILE = "export";
  private static final String UNEXPORT_FILE = "unexport";

  private Path exportPath;
  private Path unexportPath;

  /**
   * @param pin
   * @param baseDirectory
   * @param executor
   * @param watcher
   */
  public LocalPinManager(Pin pin, Path baseDirectory, ScheduledExecutorService executor,
      WatchService watcher)
  {
    super(pin, baseDirectory, executor, watcher);
  }



  /*
   * (non-Javadoc)
   * 
   * @see
   * io.github.furti.beagleio.gpio.file.FileSystemPinManager#releaseFileSystemPin(io.github.furti.
   * beagleio.Pin, java.nio.file.Path)
   */
  @Override
  protected void releaseFileSystemPin(Pin pin, Path pinDirectory) throws IOException
  {
    writeToFile(unexportPath, pin.getKernelNumber());
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * io.github.furti.beagleio.gpio.file.FileSystemPinManager#initialize(io.github.furti.beagleio.
   * Pin, java.nio.file.Path)
   */
  @Override
  protected Path initialize(Pin pin, Path baseDirectory)
  {
    this.exportPath = baseDirectory.resolve(EXPORT_FILE);
    this.unexportPath = baseDirectory.resolve(UNEXPORT_FILE);

    writeToFile(exportPath, pin.getKernelNumber());

    return baseDirectory.resolve(pin.getKernelNumber().toString());
  }
}
