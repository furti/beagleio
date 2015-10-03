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
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import io.github.furti.beagleio.BeagleIOException;
import io.github.furti.beagleio.Pin;
import io.github.furti.beagleio.gpio.PinManager;
import io.github.furti.beagleio.gpio.file.FileSystemBeagle;
import io.github.furti.beagleio.gpio.util.FileUtils;

/**
 * A Beagle implementation that creates a tmp directory on the filesystem with the same layout as
 * the GPIO Filesystem on the BeagleBone Black. This can be used on development Machines without a
 * GPIO System to thest the functionality of the application.
 * 
 * @author Daniel
 *
 */
public class TemporaryFilesystemBeagle extends FileSystemBeagle
{

  /**
   * @throws IOException If an exception occurs initializing the beagle
   */
  public TemporaryFilesystemBeagle() throws IOException
  {
    super();
  }

  @Override
  protected Path initBaseDirectory() throws IOException
  {
    Path baseDirectory = Paths.get(System.getProperty("java.io.tmpdir"), "beagleio");
    Files.createDirectories(baseDirectory);

    return baseDirectory;
  }

  @Override
  public void doRelease()
  {
    try
    {
      FileUtils.deleteDirectory(getBaseDirectory());
    } catch (IOException e)
    {
      throw new BeagleIOException("Error deleting directory " + getBaseDirectory(), e);
    }
    super.doRelease();
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
    return new TemporaryFilePinManager(pin, getBaseDirectory(), getExecutor(), getWatcher());
  }
}
