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

import io.github.furti.beagleio.gpio.AbstractBeagle;
import io.github.furti.beagleio.gpio.PinManager;

/**
 * @author Daniel
 *
 */
public class SomeTestBeagle extends AbstractBeagle
{
  /*
   * (non-Javadoc)
   * 
   * @see io.github.furti.beagleio.Beagle#release()
   */
  @Override
  public void release() throws BeagleIOException
  {
    // TODO Auto-generated method stub

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
    // TODO Auto-generated method stub
    return null;
  }

}
