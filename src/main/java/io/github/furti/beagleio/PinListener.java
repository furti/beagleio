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

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * Listens for Changes on a Pins value and executes the callbacks accordingly. The callbacks will be
 * executed immediately once when the Pins value is retrieved for the first time.
 * 
 * @author Daniel
 *
 */
public class PinListener
{
  private PinValue lastValue;
  private PollValue pollValue;
  private List<Consumer<PinValue>> callbacks = new ArrayList<>();

  /**
   * @param pin
   * @param beagle
   */
  public PinListener(Pin pin, Beagle beagle)
  {
    pollValue = beagle.poll(pin);
  }

  public PinListener whenHigh(Runnable callback)
  {
    return onChange(new PinValueConsumer(PinValue.HIGH, callback));
  }

  public PinListener whenLow(Runnable callback)
  {
    return onChange(new PinValueConsumer(PinValue.LOW, callback));
  }

  public PinListener onChange(Consumer<PinValue> callback)
  {
    callbacks.add(callback);
    return this;
  }

  /**
   * Execute the listener to dedect changes
   */
  void execute()
  {
    PinValue actualValue = pollValue.getValue();

    if (actualValue != lastValue)
    {
      for (Consumer<PinValue> callback : callbacks)
      {
        callback.accept(actualValue);
      }

      lastValue = actualValue;
    }
  }

  /**
   * @author Daniel
   *
   */
  private static class PinValueConsumer implements Consumer<PinValue>
  {
    private PinValue expectedValue;
    private Runnable callback;

    public PinValueConsumer(PinValue expectedValue, Runnable callback)
    {
      super();
      this.expectedValue = expectedValue;
      this.callback = callback;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.util.function.Consumer#accept(java.lang.Object)
     */
    @Override
    public void accept(PinValue value)
    {
      if (expectedValue.equals(value))
      {
        callback.run();
      }
    }
  }
}
