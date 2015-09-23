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
public class BeagleIOException extends RuntimeException
{

  private static final long serialVersionUID = -4650832485749277803L;

  /**
   * Constructs a new BeagleIOException with an message and the underlying cause.
   * 
   * @param message the detail message (which is saved for later retrieval by the getMessage()
   *        method).
   * @param cause the reason for this exception
   */
  public BeagleIOException(String message, Throwable cause)
  {
    super(message, cause);
  }
}
