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
 * List of all Available directions for a pin.
 * 
 * @author Daniel
 *
 */
public enum Direction
{
  /**
   * Sets the direction to input. So we can read from the pin later.
   */
  IN("in"),

  /**
   * Sets the direction to output. So we can write to the pin later. Prefer the OUT_HIGHT and
   * OUT_LOW values over this. So you can set the right value immediate without waiting for the
   * programm to continue. Otherwise you might have unexpected behaviour until the values for all
   * your pins are set.
   */
  OUT("out"),

  /**
   * Sets the direction to ouput and the value of the pin to high.
   */
  OUT_HIGH("hight"),

  /**
   * Sets the direction to output and the value of the pin to low.
   */
  OUT_LOW("low");

  private String value;

  private Direction(String value)
  {
    this.value = value;
  }

  public String getValue()
  {
    return value;
  }
}
