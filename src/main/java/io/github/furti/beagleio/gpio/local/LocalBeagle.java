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
import java.nio.file.Paths;

import io.github.furti.beagleio.Pin;
import io.github.furti.beagleio.gpio.PinManager;
import io.github.furti.beagleio.gpio.file.FileSystemBeagle;

/**
 * @author Daniel
 *
 */
public class LocalBeagle extends FileSystemBeagle
{

  /**
   * @throws IOException if an exception occurs initializing the Beagle
   */
  public LocalBeagle() throws IOException
  {
    super();
  }

  /*
   * (non-Javadoc)
   * 
   * @see io.github.furti.beagleio.gpio.file.FileSystemBeagle#initBaseDirectory()
   */
  @Override
  protected Path initBaseDirectory() throws IOException
  {
    return Paths.get("/sys/class/gpio");
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
    return null;
  }
}
