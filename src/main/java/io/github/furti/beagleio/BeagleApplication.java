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
 * Main Entry Point for BeagleIO Applications.
 * 
 * @author Daniel
 *
 */
public abstract class BeagleApplication
{
  private Beagle beagle;
  private boolean running;

  /**
   * Actually starts the application.
   * 
   * This means that a Beagle will be created, the initialization process is started and the
   * Application loop will be initialized.
   * 
   * @param args Arguments passed to the Application on startup.
   */
  protected final void start(String[] args)
  {
    beagle = setupBeagle();

    try
    {
      initialize(beagle);

      startLoop();
    } finally
    {
      beagle.release();
    }
  }

  /**
   * Implementations may override this method to provide a custom Beagle implementation.
   * 
   * <p>
   * By default {@link DogHouse#callDog()} is used to get a Beagle implementation. See it's javadoc
   * for more details on the default Beagle creation.
   * </p>
   * 
   * @return The Beagle instance to use for this application.
   */
  protected Beagle setupBeagle()
  {
    return DogHouse.callDog();
  }

  /**
   * @return The Beagle instance used for this application.
   */
  protected final Beagle getBeagle()
  {
    return beagle;
  }

  /**
   * This is the place to do initialization stuff. For Example one can initialize all required Pins
   * here.
   * 
   * <p>
   * It is possible to initialize Pins later. But if all Pins are initialized here we can ensure
   * that everything works when we access the Pins later on.
   * </p>
   * 
   * @param beagle The Beagle instance used by this application.
   */
  protected abstract void initialize(Beagle beagle);

  /**
   * The actual code for the Application will be implemented in the run Method. This Method is
   * called once for each iteration in the main loop.
   * 
   * <p>
   * The run Method should return true when the application should continue with the next iteration.
   * If the exit condition is met simply return false from this method and the Application will
   * cleanup and exit.
   * </p>
   * 
   * @param beagle The Beagle instance used by this application.
   * @return true if the application should continue, false otherwise.
   */
  protected abstract boolean run(Beagle beagle);

  /**
   * Applications can perform cleanup code when the application is shutting down.
   * 
   * <b>It is not neccessary to call beagle.release() here. This is done by the BeagleApplication
   * itself.</b>
   * 
   * @param beagle The Beagle instance used by this application.
   */
  protected abstract void cleanup(Beagle beagle);

  /**
   * Starts the main loop that executes the application.
   */
  private void startLoop()
  {
    running = true;

    while (running)
    {
      running = run(beagle);
    }

    cleanup(beagle);
  }

  /**
   * Launches the Application specified by the appClass argument. This method blocks until the
   * application exits.
   * 
   * <p>
   * A typical Usecase is:
   * 
   * <pre>
   * public static void main(String[] args)
   * {
   *   BeagleApplication.launch(MyBeagleApp.class, args);
   * }
   * </pre>
   * </p>
   * 
   * @param appClass The application Class to instantiate.
   * @param args Arguments for the Application
   * @throws IllegalArgumentException if the appClass is not an Instance of
   *         {@link BeagleApplication}
   * @throws BeagleIOException if an Exception occurs while instantiating the Application
   */
  public static <T extends BeagleApplication> void launch(Class<T> appClass, String[] args)
      throws IllegalArgumentException, BeagleIOException
  {
    if (!BeagleApplication.class.isAssignableFrom(appClass))
    {
      throw new IllegalArgumentException("Class " + appClass + " is not an instance of "
          + BeagleApplication.class);
    }

    try
    {
      T application = appClass.getConstructor().newInstance();
      application.start(args);
    } catch (Exception e)
    {
      throw new BeagleIOException("An Error occured while instantiating the Application", e);
    }
  }
}
