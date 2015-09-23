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

import io.github.furti.beagleio.Beagle;
import io.github.furti.beagleio.BeagleIOException;
import io.github.furti.beagleio.Direction;

/**
 * A Beagle implementation that creates a tmp directory on the filesystem with the same layout as
 * the GPIO Filesystem on the BeagleBone Black. This can be used on development Machines without a
 * GPIO System to thest the functionality of the application.
 * 
 * @author Daniel
 *
 */
public class TemporaryFilesystemBeagle implements Beagle
{
  private Path baseDirectory;

  /**
   * @throws IOException if an exception occurs creating the tmp directory.
   * 
   */
  public TemporaryFilesystemBeagle() throws IOException
  {
    this.setupTmpDirectory();
  }

  /*
   * (non-Javadoc)
   * 
   * @see io.github.furti.beagleio.Beagle#initializePin(io.github.furti.beagleio.Direction, boolean)
   */
  @Override
  public void initializePin(Direction direction, boolean activeLow) throws BeagleIOException
  {
    // TODO Auto-generated method stub

  }

  /*
   * (non-Javadoc)
   * 
   * @see io.github.furti.beagleio.Beagle#closePin()
   */
  @Override
  public void closePin() throws BeagleIOException
  {
    // TODO Auto-generated method stub

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
  public void release() throws BeagleIOException
  {
    try
    {
      Files.deleteIfExists(baseDirectory);
    } catch (IOException e)
    {
      throw new BeagleIOException("Error deleting tmp directory", e);
    }
  }
}
