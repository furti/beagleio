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
  private List<OutstandingOperation> operations = new ArrayList<>();

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

  @Override
  public PinManager release()
  {
    addOperation(this::doRelease);
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
    Iterator<OutstandingOperation> iterator = operations.iterator();

    while (iterator.hasNext())
    {
      OutstandingOperation operation = iterator.next();
      operation.perform();
      iterator.remove();
    }

    return this;
  }

  /**
   * Add an operation to the queue that should be performed the next time the
   * {@link #performOutstandingOperations()} method is called. The argument is passed as a parameter
   * to the consumer when the operation is called.
   * 
   * @param consumer operation to call.
   * @param argument to use for the call of the consumer.
   */
  protected <T> void addOperation(Consumer<T> consumer, T argument)
  {
    operations.add(new OutstandingConsumer<T>(consumer, argument));
  }

  /**
   * Add an operation to the queue that should be performed the next time the
   * {@link #performOutstandingOperations()} method is called. The operation does not need any
   * parameters.
   * 
   * @param consumer operation to call.
   * @param argument to use for the call of the consumer.
   */
  protected <T> void addOperation(OutstandingOperation operation)
  {
    operations.add(operation);
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
   * Let the implementation actually release all open Resources.
   */
  protected abstract void doRelease();

  /**
   * @author Daniel
   *
   * @param <T> type of argument
   */
  public static class OutstandingConsumer<T> implements OutstandingOperation
  {
    private Consumer<T> consumer;
    private T argument;

    public OutstandingConsumer(Consumer<T> consumer, T argument)
    {
      super();
      this.consumer = consumer;
      this.argument = argument;
    }

    @Override
    public void perform()
    {
      consumer.accept(argument);
    }
  }
}
