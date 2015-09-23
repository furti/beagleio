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

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import io.github.furti.beagleio.Direction;
import io.github.furti.beagleio.Pin;
import io.github.furti.beagleio.gpio.GpioConstants;

/**
 * Wraps the underlying files for the GPIO pin and exposes some functionality to configure, read and
 * write the pin.
 * 
 * @author Daniel
 *
 */
public class PinDirectory
{
  private Path directionPath;
  private Path valuePath;

  public PinDirectory(Pin pin)
  {
    super();

    Path pinDirectory = Paths.get(GpioConstants.BASE_DIRECTORY, "gpio",
        pin.getKernelNumber().toString());

    this.directionPath = pinDirectory.resolve("direction");
    this.valuePath = pinDirectory.resolve("value");
  }

  /**
   * Sets the direction of the file.
   * 
   * @param direction to set
   * @throws IOException if an execption occurs accessing the direction file
   */
  public void setDirection(Direction direction) throws IOException
  {
    try (BufferedWriter out = Files.newBufferedWriter(directionPath, StandardOpenOption.WRITE))
    {
      out.write(direction.getValue());
    }
  }


}
