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

import io.github.furti.beagleio.Direction;
import io.github.furti.beagleio.PinValue;

/**
 * Interface that encapsulates the operations that are available on a pin.
 * 
 * Each operation that returns the PinManager for chaining must not be executed immediate but must
 * be queued instead. The queued operations are then executed when the
 * {@link #performOutstandingOperations()} method is called. This is in respect to the remote Beagle
 * implemenation. So we can chain some operations and send them together over the wire.
 * 
 * @author Daniel
 *
 */
public interface PinManager
{

  /**
   * Sets the direction of the pin.
   * 
   * @param direction the direction to set
   * @return the instance for a fluent API
   */
  PinManager setDirection(Direction direction);

  /**
   * Sets the activeLow value.
   * 
   * @param activeLow the value to set
   * @return the instance for a fluent API
   */
  PinManager setActiveLow(boolean activeLow);


  /**
   * @param value The value to set for the pin.
   * @return the instance for a fluent API
   */
  PinManager setValue(PinValue value);

  /**
   * 
   * @return The value of the pin
   */
  PinValue getValue();

  /**
   * Releases the Pin as it is not in use anymore.
   * 
   * <p>
   * This should clean up all open Resources for this Pin.
   * </p>
   * 
   * @return the instance for a fluent API
   */
  PinManager release();

  /**
   * Executes all the queued operations.
   * 
   * @return the instance for a fluent API
   */
  PinManager performOutstandingOperations();
}
