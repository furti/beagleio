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
package io.github.furti.beagleio.gpio.temporary;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.WatchService;
import java.nio.file.attribute.PosixFilePermission;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ScheduledExecutorService;

import io.github.furti.beagleio.BeagleIOException;
import io.github.furti.beagleio.Pin;
import io.github.furti.beagleio.gpio.file.FileSystemPinManager;
import io.github.furti.beagleio.gpio.util.FileUtils;

/**
 * @author Daniel
 *
 */
public class TemporaryFilePinManager extends FileSystemPinManager
{

  public TemporaryFilePinManager(Pin pin, Path baseDirectory, ScheduledExecutorService executor,
      WatchService watcher)
  {
    super(pin, baseDirectory, executor, watcher);
  }

  /**
   * Creates the directory for the pin and all files that are needed for the gpio system to work.
   */
  protected Path initialize(Pin pin, Path baseDirectory)
  {
    Path pinDirectory = null;
    try
    {
      pinDirectory = baseDirectory.resolve(pin.toString());

      if (!Files.exists(pinDirectory))
      {
        Files.createDirectory(pinDirectory);
        createPermissions(pinDirectory);
      }

      createIfNotExists(pinDirectory, ACTIVE_LOW_FILE);
      createIfNotExists(pinDirectory, DIRECTION_FILE);
      createIfNotExists(pinDirectory, EDGE_FILE);
      createIfNotExists(pinDirectory, POWER_FILE);
      createIfNotExists(pinDirectory, UEVENT_FILE);

      // Initialize the value with LOW
      writeToFile(createIfNotExists(pinDirectory, VALUE_FILE), "0");

      return pinDirectory;
    } catch (IOException e)
    {
      throw new BeagleIOException("Error initializing Temporary Pin Directory " + pinDirectory, e);
    }
  }

  private Path createIfNotExists(Path directory, String fileName) throws IOException
  {
    Path file = directory.resolve(fileName);

    if (!Files.exists(file))
    {
      Files.createFile(file);

      createPermissions(file);
    }

    return file;
  }

  private void createPermissions(Path path) throws IOException
  {
    if (FileUtils.IS_POSIX)
    {
      Set<PosixFilePermission> permissions = new HashSet<>(3);
      permissions.add(PosixFilePermission.OWNER_READ);
      permissions.add(PosixFilePermission.OWNER_WRITE);
      permissions.add(PosixFilePermission.OWNER_EXECUTE);
      Files.setPosixFilePermissions(path, permissions);
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see io.github.furti.beagleio.gpio.file.FileSystemPinManager#releaseFileSystemPin()
   */
  @Override
  protected void releaseFileSystemPin(Pin pin, Path pinDirectory) throws IOException
  {
    FileUtils.deleteDirectory(pinDirectory);
  }
}
