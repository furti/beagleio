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

import io.github.furti.beagleio.PinValue;
import io.github.furti.beagleio.PollValue;

/**
 * @author Daniel
 *
 */
public class DefaultPollValue implements PollValue
{
  private PinValue value;

  public DefaultPollValue(PinValue value)
  {
    super();
    this.value = value;
  }

  @Override
  public PinValue getValue()
  {
    return value;
  }

  public void setValue(PinValue value)
  {
    this.value = value;
  }
}
