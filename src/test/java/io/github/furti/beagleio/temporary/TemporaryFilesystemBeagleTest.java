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
package io.github.furti.beagleio.temporary;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.testng.annotations.AfterTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import io.github.furti.beagleio.Beagle;
import io.github.furti.beagleio.Direction;
import io.github.furti.beagleio.Pin;
import io.github.furti.beagleio.gpio.temporary.TemporaryFilesystemBeagle;
import io.github.furti.beagleio.gpio.util.FileUtils;

/**
 * @author Daniel
 *
 */
public class TemporaryFilesystemBeagleTest
{
  private Path tmpDir = Paths.get(System.getProperty("java.io.tmpdir"), "beagleio");

  @Test
  public void tmpDirCreatedOnInstantiation() throws IOException
  {
    new TemporaryFilesystemBeagle();

    assertThat("Tmp Directory should exist", Files.exists(tmpDir), equalTo(true));
  }

  @Test
  public void tmpDirRemovedOnRelease() throws IOException
  {
    Beagle beagle = new TemporaryFilesystemBeagle();
    beagle.release();

    assertThat("Tmp Directory should not exist anymore", Files.exists(tmpDir), equalTo(false));
  }

  @Test(dataProvider = "pinDirectoriesCreatedData")
  public void pinDirectoryCreated(Pin pin) throws IOException
  {
    Beagle beagle = new TemporaryFilesystemBeagle();

    beagle.initializePin(pin, Direction.IN);

    Path pinDirectory = tmpDir.resolve(pin.toString());

    fileExists(pinDirectory, "Pin Directory created");
    fileExists(pinDirectory.resolve("active_low"), "active_low created");
    fileExists(pinDirectory.resolve("direction"), "direction created");
    fileExists(pinDirectory.resolve("edge"), "edge created");
    fileExists(pinDirectory.resolve("power"), "power created");
    fileExists(pinDirectory.resolve("uevent"), "uevent created");
    fileExists(pinDirectory.resolve("value"), "value created");
  }

  @Test(dataProvider = "pinInitializedData")
  public void pinInitialized(Pin pin, Direction direction, boolean activeLow) throws IOException
  {
    Beagle beagle = new TemporaryFilesystemBeagle();

    beagle.initializePin(pin, direction, activeLow);

    Path pinDirectory = tmpDir.resolve(pin.toString());

    hasContent(pinDirectory.resolve("direction"), direction.getValue(), "Direction: ");
    hasContent(pinDirectory.resolve("active_low"), activeLow ? "1" : "0", "ActiveLow: ");
  }

  @DataProvider
  public Object[][] pinDirectoriesCreatedData()
  {
    return new Object[][] {
        {Pin.P8_03},
        {Pin.P9_13}
    };
  }

  @DataProvider
  public Object[][] pinInitializedData()
  {
    return new Object[][] {
        {Pin.P8_06, Direction.IN, false}
    };
  }

  @AfterTest
  public void cleanup() throws IOException
  {
    FileUtils.deleteDirectory(tmpDir);
  }

  private void fileExists(Path path, String message)
  {
    assertThat(message, Files.exists(path), equalTo(true));
  }

  private void hasContent(Path file, String expectedContent, String message) throws IOException
  {
    List<String> actualContent = Files.readAllLines(file);

    assertThat(message, actualContent, notNullValue());
    assertThat(message, actualContent.size(), equalTo(1));
    assertThat(message, actualContent.get(0), equalTo(expectedContent));
  }
}
