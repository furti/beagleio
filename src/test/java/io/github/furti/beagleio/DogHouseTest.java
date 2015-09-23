package io.github.furti.beagleio;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import io.github.furti.beagleio.gpio.inmemory.InMemoryBeagle;
import io.github.furti.beagleio.gpio.local.LocalBeagle;
import io.github.furti.beagleio.gpio.remote.RemoteBeagle;

public class DogHouseTest
{

  @Test(dataProvider = "callDogByPropertyData")
  public void callDogByProperty(String systemProperty, Class<?> expectedDogClass)
  {
    if (systemProperty != null)
    {
      System.setProperty(DogHouse.DOG_CLASS_PROPERTY, systemProperty);
    }

    try
    {
      Beagle actualBeagle = DogHouse.callDog();
      assertThat(actualBeagle.getClass(), equalTo(expectedDogClass));
    } finally
    {
      System.clearProperty(DogHouse.DOG_CLASS_PROPERTY);
    }
  }

  @Test(expectedExceptions = BeagleIOException.class,
      expectedExceptionsMessageRegExp = "Error calling for dog by property unknowndog")
  public void unknownProperty()
  {
    System.setProperty(DogHouse.DOG_CLASS_PROPERTY, "unknowndog");

    try
    {
      DogHouse.callDog();
    } finally
    {
      System.clearProperty(DogHouse.DOG_CLASS_PROPERTY);
    }
  }

  @Test
  public void callDogByClass()
  {
    Beagle beagle = DogHouse.callDog(InMemoryBeagle.class);

    assertThat(beagle.getClass(), equalTo(InMemoryBeagle.class));
  }

  @Test(expectedExceptions = BeagleIOException.class)
  public void notABeagleClass()
  {
    DogHouse.callDog(String.class);
  }

  @DataProvider
  public Object[][] callDogByPropertyData()
  {
    return new Object[][] {
        {null, LocalBeagle.class},
        {"inmemory", InMemoryBeagle.class},
        {"local", LocalBeagle.class},
        {"remote", RemoteBeagle.class},
        {"io.github.furti.beagleio.SomeTestBeagle", SomeTestBeagle.class}
    };
  }
}
