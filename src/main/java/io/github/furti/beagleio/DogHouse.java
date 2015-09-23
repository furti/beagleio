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

import io.github.furti.beagleio.gpio.inmemory.InMemoryBeagle;
import io.github.furti.beagleio.gpio.local.LocalBeagle;
import io.github.furti.beagleio.gpio.remote.RemoteBeagle;

/**
 * Factory for creating Beagles :)
 * 
 * @author Daniel
 *
 */
public final class DogHouse
{
  private static final Class<?> DEFAULT_DOG_CLASS = LocalBeagle.class;
  public static final String DOG_CLASS_PROPERTY = "beagleio.dogclass";

  private DogHouse()
  {

  }

  /**
   * Creates a implementation of an Beagle based on the Systemproperty <b>beagleio.dogclass</b>
   * 
   * beagleio.dogclass specifies the full Classname of a class implementing the {@link Beagle}
   * interface.
   * 
   * There are three shortcuts available for this property.
   * 
   * <ul>
   * <li><b>local</b>: Instantiates a {@link LocalBeagle} that operates on the local System</li>
   * <li><b>remote</b>: Instantiates a {@link RemoteBeagle} that sends all operations over the
   * network to an remote BeagleBone</li>
   * <li><b>inmemory</b>: Instantiates A {@link InMemoryBeagle} that is a dummy implementation for
   * Development Systems that do not contain a GPIO System.</li>
   * </ul>
   * 
   * @return a new Beagle to use for accessing the GPIO System.
   * @throws BeagleIOException if an execption occurs initializing the Beagle class.
   */
  public static Beagle callDog() throws BeagleIOException
  {
    String dogClassProperty = System.getProperty(DOG_CLASS_PROPERTY);
    Class<?> dogClass = null;

    if (dogClassProperty == null)
    {
      dogClass = DEFAULT_DOG_CLASS;
    } else
    {
      switch (dogClassProperty)
      {
        case "local":
          dogClass = LocalBeagle.class;
          break;
        case "inmemory":
          dogClass = InMemoryBeagle.class;
          break;
        case "remote":
          dogClass = RemoteBeagle.class;
          break;
        default:
          try
          {
            dogClass = Class.forName(dogClassProperty);
          } catch (ClassNotFoundException e)
          {
            throw new BeagleIOException("Error calling for dog by property " + dogClassProperty, e);
          }
      }
    }

    return callDog(dogClass);
  }

  /**
   * Instantiates a new Beagle for the dogClass.
   * 
   * @param dogClass the Class of Beagle to instantiate.
   * @return a new Beagle to use for operations on the GPIO System.
   */
  public static Beagle callDog(Class<?> dogClass)
  {
    try
    {
      return (Beagle) dogClass.newInstance();
    } catch (Exception e)
    {
      throw new BeagleIOException("Error calling for dog by class " + dogClass, e);
    }
  }
}
