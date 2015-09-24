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
package io.github.furti.beagleio.gpio.util;

import io.github.furti.beagleio.BeagleIOException;

/**
 * Assertion Helpers that throw a {@link BeagleIOException}.
 * 
 * @author Daniel
 *
 */
public final class BeagleAssert
{

  private BeagleAssert()
  {

  }

  /**
   * If the object is not null a {@link BeagleIOException} will thrown.
   * 
   * @param o object to check
   * @param message for the exception
   * @param arguments for the message
   */
  public static void isNull(Object o, String message, Object... arguments)
  {
    if (o != null)
    {
      doThrow(message, arguments);
    }
  }

  /**
   * If the object is null a {@link BeagleIOException} will be thrown.
   * 
   * @param o object to check
   * @param message for the exception
   * @param arguments for the message
   */
  public static void isNotNull(Object o, String message, Object... arguments)
  {
    if (o == null)
    {
      doThrow(message, arguments);
    }
  }

  /**
   * @param message
   * @param arguments
   */
  private static void doThrow(String message, Object[] arguments)
  {
    if (arguments != null && arguments.length > 0)
    {
      throw new BeagleIOException(message, null);
    } else
    {
      throw new BeagleIOException(String.format(message, arguments), null);
    }
  }
}
