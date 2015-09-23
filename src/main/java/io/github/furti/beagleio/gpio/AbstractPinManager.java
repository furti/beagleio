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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;

import io.github.furti.beagleio.Direction;

/**
 * @author Daniel
 *
 */
public abstract class AbstractPinManager implements PinManager
{
  private List<OutstandingOperation<?>> operations = new ArrayList<>();

  /*
   * (non-Javadoc)
   * 
   * @see io.github.furti.beagleio.gpio.PinManager#setDirection(io.github.furti.beagleio.Direction)
   */
  @Override
  public PinManager setDirection(Direction direction)
  {
    addOperation(this::doSetDirection, direction);
    return this;
  }

  /*
   * (non-Javadoc)
   * 
   * @see io.github.furti.beagleio.gpio.PinManager#setActiveLow(boolean)
   */
  @Override
  public PinManager setActiveLow(boolean activeLow)
  {
    addOperation(this::doSetActiveLow, activeLow);
    return this;
  }

  /*
   * (non-Javadoc)
   * 
   * @see io.github.furti.beagleio.gpio.PinManager#performOutstandingOperations()
   */
  @Override
  public PinManager performOutstandingOperations()
  {
    Iterator<OutstandingOperation<?>> iterator = operations.iterator();

    while (iterator.hasNext())
    {
      OutstandingOperation<?> operation = iterator.next();
      operation.perform();
      iterator.remove();
    }

    return this;
  }

  /**
   * Add an operation to the queue that should be performed the next time the
   * {@link #performOutstandingOperations()} method is called.
   * 
   * @param consumer operation to call.
   * @param argument to use for the call of the consumer.
   */
  protected <T> void addOperation(Consumer<T> consumer, T argument)
  {
    operations.add(new OutstandingOperation<T>(consumer, argument));
  }

  /**
   * Let the implementation actually set the direction.
   * 
   * @param direction the direction to set
   */
  protected abstract void doSetDirection(Direction direction);

  /**
   * Let the implementation acutally set the activeLow value.
   * 
   * @param activeLow the activeLow value to set
   */
  protected abstract void doSetActiveLow(boolean activeLow);

  /**
   * @author Daniel
   *
   * @param <T> type of argument
   */
  public static class OutstandingOperation<T>
  {
    private Consumer<T> consumer;
    private T argument;

    public OutstandingOperation(Consumer<T> consumer, T argument)
    {
      super();
      this.consumer = consumer;
      this.argument = argument;
    }

    /**
     * Performs the actual operation
     */
    public void perform()
    {
      consumer.accept(argument);
    }
  }
}
