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

/**
 * Interface to communicate with the GPIO System on a BeagleBone Black.
 * 
 * @author Daniel
 *
 */
public interface Beagle
{

  /**
   * Initializes the Pin with activeLow = false.
   *
   * @see #initializePin(Pin, Direction, boolean) initializePin(Pin, Direction, boolean) for further
   *      details
   */
  default void initializePin(Pin pin, Direction direction)
  {
    this.initializePin(pin, direction, false);
  }

  /**
   * Initializes all the pins in the {@link PinGroup} with activeLow = false.
   * 
   * @param pins the pins to initialize
   * 
   * @see #initializePin(Pin, Direction, boolean) initializePin(Pin, Direction, boolean) for further
   *      details
   */
  default void initializePins(PinGroup pins, Direction direction)
  {
    for (Pin pin : pins.getPins())
    {
      this.initializePin(pin, direction);
    }
  }

  /**
   * A Pin needs to be initialized before it can be used.
   * 
   * <p>
   * Two steps are required to initialize a Pin.
   * <ul>
   * <li>If the Pin is not exported yet it has to be exported to be used.</li>
   * <li>After a Pin is exported the direction the Pin is working on must be set.</li>
   * </ul>
   * Bot steps are handled by the {@link #initializePin(Direction)} method.
   * </p>
   * 
   * <p>
   * <b>If you don't initialize a Pin all further actions on that Pin will throw an
   * {@link BeagleIOException}</b>
   * <p>
   * 
   * @param pin the Pin to initialize.
   * @param direction the direction to set for the Pin. When setting it to {@link Direction#OUT}
   *        have a look at it's JavaDoc for further details.
   * @param activeLow if set to true the logic for hight and low for this Pin will be reversed. So
   *        3.3V on the Pin will be LOW and 0V on the Pin will be HIGH.
   * @throws BeagleIOException if an exeption occurs configuring the Pin.
   */
  void initializePin(Pin pin, Direction direction, boolean activeLow) throws BeagleIOException;

  /**
   * Initializes all the pins in the {@link PinGroup}.
   * 
   * @param pins the pins to initialize
   * 
   * @see #initializePin(Pin, Direction, boolean) initializePin(Pin, Direction, boolean) for further
   *      details
   */
  default void initializePins(PinGroup pins, Direction direction, boolean activeLow)
  {
    for (Pin pin : pins.getPins())
    {
      this.initializePin(pin, direction, activeLow);
    }
  }

  /**
   * Closes the Pin so that it can be used for other purposes.
   * 
   * @param pin the Pin to close.
   * @throws BeagleIOException if an execption occurs while closing the Pin.
   */
  void closePin(Pin pin) throws BeagleIOException;

  /**
   * Closes the Pins so that they can be used for other purposes.
   * 
   * @param pins the Pins to close.
   * @throws BeagleIOException if an execption occurs while closing the Pins.
   */
  default void closePins(PinGroup pins) throws BeagleIOException
  {
    for (Pin pin : pins.getPins())
    {
      this.closePin(pin);
    }
  }

  /**
   * Releases the Beagle and closes all used Resources.
   * 
   * @throws BeagleIOException if an exception occurs closing used Resources.
   */
  void release() throws BeagleIOException;
}
