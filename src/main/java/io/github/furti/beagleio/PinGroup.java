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
package io.github.furti.beagleio;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * A {@link PinGroup} can be used if you want to perform some operations on the same set of pins
 * over and over again.
 * 
 * <p>
 * e.g. if you have three LEDs on three different pins and want them to blink you can create a
 * PinGroup for the three Pins and use it to trigger the LEDs.
 * </p>
 * 
 * @author Daniel
 *
 */
public class PinGroup
{
  private List<Pin> pins;

  private PinGroup(List<Pin> pins)
  {
    this.pins = pins;
  }

  /**
   * @return A unmodifiable list of Pins in this group.
   */
  public List<Pin> getPins()
  {
    return pins;
  }

  /**
   * Create a PinGroup for the list of pins. Pins will be processed in the order they are specified.
   * 
   * @param pins Pins to add
   * @return the {@link PinGroup}
   */
  public static PinGroup fromPins(Pin... pins)
  {
    return new PinGroup(Collections.unmodifiableList(Arrays.asList(pins)));
  }
}
