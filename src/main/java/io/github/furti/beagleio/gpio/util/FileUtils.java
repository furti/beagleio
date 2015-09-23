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
package io.github.furti.beagleio.gpio.util;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;

/**
 * @author Daniel
 *
 */
public final class FileUtils
{
  public static final boolean IS_POSIX =
      FileSystems.getDefault().supportedFileAttributeViews().contains("posix");

  private FileUtils()
  {

  }

  /**
   * Recursivle reads the content of the directory and deletes it. After the directory is empty it
   * will be deleted;
   * 
   * @param path Directory to delete.
   * @throws IOException
   */
  public static void deleteDirectory(Path directory) throws IOException
  {
    Files.walkFileTree(directory, new FileVisitor<Path>()
    {

      @Override
      public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs)
          throws IOException
      {
        return FileVisitResult.CONTINUE;
      }

      @Override
      public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException
      {
        Files.delete(file);

        return FileVisitResult.CONTINUE;
      }

      @Override
      public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException
      {
        return FileVisitResult.CONTINUE;
      }

      @Override
      public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException
      {
        Files.delete(dir);
        return FileVisitResult.CONTINUE;
      }
    });
  }
}
