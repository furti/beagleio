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
public enum PinValue
{
  HIGH("1"), LOW("0");

  private String value;

  private PinValue(String value)
  {
    this.value = value;
  }

  public String getValue()
  {
    return value;
  }

  /**
   * @param readFromFile
   * @return
   */
  public static PinValue forValue(String value)
  {
    for (PinValue pinValue : PinValue.values())
    {
      if (pinValue.getValue().equals(value))
      {
        return pinValue;
      }
    }

    throw new BeagleIOException("PinValue " + value + " not found", null);
  }
}
