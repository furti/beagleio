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

import java.util.HashMap;
import java.util.Map;

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
   * @param pin the pin to set the value for.
   * @param value The value to set for the pin.
   */
  void setPinValue(Pin pin, PinValue value);

  /**
   * @param pins The Pins to set the value for
   * @param value The value to set for the pins
   */
  default void setPinsValue(PinGroup pins, PinValue value)
  {
    for (Pin pin : pins.getPins())
    {
      this.setPinValue(pin, value);
    }
  }

  /**
   * @param pin The Pin to read the value from
   * @return The Pins current value
   */
  PinValue getPinValue(Pin pin);

  /**
   * @param pins The Pins to get the Value for
   * @return A Map that contains the Pins and their values
   */
  default Map<Pin, PinValue> getPinsValue(PinGroup pins)
  {
    Map<Pin, PinValue> result = new HashMap<>();

    for (Pin pin : pins.getPins())
    {
      result.put(pin, this.getPinValue(pin));
    }

    return result;
  }

  /**
   * Unlike {@link #getPinValue(Pin)} poll listens for changes on the Pins value and updates the
   * {@link PollValue} accordingly. Depending on the operating system and the duration of a value
   * change it is possible that some changes will be lost.
   * 
   * <p>
   * For example when a Pin is changes it's state from LOW to HIGH for 10 milliseconds and there is
   * no native hook or the Pins value is not polled in this time span, then this change will not be
   * detected.
   * </p>
   * 
   * <p>
   * But this might as well happen with {@link #getPinValue(Pin)}. Nevertheless {@link #poll(Pin)}
   * might be the better choice over {@link #getPinValue(Pin)} because when available native hooks
   * are used to dedect changes and the Pins value must be read only when such a change occurs.
   * </p>
   * 
   * <p>
   * If not stated otherwise it is safe to call this Method everytime you need the polled value as
   * implementations should cache a {@link PollValue}.
   * </p>
   * 
   * @param pin The Pin to poll.
   * @return A {@link PollValue} that is updated with the Pins actual value every time it changes.
   */
  PollValue poll(Pin pin);

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
