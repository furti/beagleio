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
package io.github.furti.beagleio.gpio;

import static io.github.furti.beagleio.gpio.util.BeagleAssert.isNotNull;
import static io.github.furti.beagleio.gpio.util.BeagleAssert.isNull;

import java.util.HashMap;
import java.util.Map;

import io.github.furti.beagleio.Beagle;
import io.github.furti.beagleio.BeagleIOException;
import io.github.furti.beagleio.Direction;
import io.github.furti.beagleio.Pin;

/**
 * Base implementation of a Beagle that handles some common functionality.
 * 
 * @author Daniel
 *
 */
public abstract class AbstractBeagle implements Beagle
{
  private Map<Pin, PinManager> pins = new HashMap<>();

  /*
   * (non-Javadoc)
   * 
   * @see io.github.furti.beagleio.Beagle#initializePin(io.github.furti.beagleio.Direction, boolean)
   */
  @Override
  public void initializePin(Pin pin, Direction direction, boolean activeLow)
      throws BeagleIOException
  {
    isNull(pins.get(pin), "Pin %s is already initilized", pin);

    PinManager pinManager = createPinManager(pin);
    pinManager
        .setDirection(direction)
        .setActiveLow(activeLow)
        .performOutstandingOperations();

    pins.put(pin, pinManager);
  }

  /*
   * (non-Javadoc)
   * 
   * @see io.github.furti.beagleio.Beagle#closePin()
   */
  @Override
  public void closePin(Pin pin) throws BeagleIOException
  {
    findPinManager(pin)
        .release()
        .performOutstandingOperations();

  }

  /**
   * Retrieves the already initialized pin and throws an exception if it was not found.
   */
  private PinManager findPinManager(Pin pin)
  {
    PinManager pinManager = pins.get(pin);

    isNotNull(pinManager,
        "Pin %s was not found. Did you forgot to initialize it? Always call beagle.initializePin(pin, direction) before using a Pin.",
        pin);

    return pinManager;
  }

  /**
   * @param pin
   * @return
   */
  protected abstract PinManager createPinManager(Pin pin);
}
