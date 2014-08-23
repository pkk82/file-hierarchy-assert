file-hierarchy-assert
=====================

A small library to check file hierarchy state with assertj assertions

With file hierarchy

```
\---fileHierarchy
    |   file1 // content lines: <empty>
    |   file2 // content lines: <empty>
    |
    +---dir1
    |   |   file11 // content line: file11
    |   |
    |   +---dir11
    |   \---dir12
    |           file121 // content line: file121, file121
    |
    \---dir2
        +---dir21
        |       file211 // content lines: file211, file211
        |       file212 // content lines: file212, file212
        |
        \---dir22
            \---dir221
                |   file2211 // content lines: file2211, file2211, file2211
                |
                \---dir2211
                    |   file22111 // content lines: file22111, file22111, file22111, file22111
                    |   file22112 // content lines: file22112, file22112, file22112, file22112
                    |
                    \---dir22111

```
assertions are correct
```java
fileHierarchyAssert.hasRootDirWithName("fileHierarchy");
fileHierarchyAssert.hasCountOfSubdirs(9);
fileHierarchyAssert.hasCountOfSubdirs(2, "dir1");
fileHierarchyAssert.hasCountOfFiles(9);
fileHierarchyAssert.hasCountOfFiles(3, "dir2", "dir22", "dir221");
fileHierarchyAssert.hasCountOfFilesAndDirs(19);
fileHierarchyAssert.hasCountOfFilesAndDirs(1, "dir1", "dir11", "dir221");
fileHierarchyAssert.containsSubdir("dir111", "dir1", "dir11");
fileHierarchyAssert.containsFile("file11", "dir1");
fileHierarchyAssert.containsFileWithContent("file212", Arrays.asList("file212", "file212"), "dir2", "dir21");
```
