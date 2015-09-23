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
 * @author Daniel
 *
 */
public class SomeTestBeagle implements Beagle
{

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

}
