file-hierarchy-assert
=====================

A small library to check file hierarchy state with assertj assertions

## Maven use

```xml
<dependency>
	<groupId>pl.pkk82.file-hierarchy-assert</groupId>
	<artifactId>file-hierarchy-assert</artifactId>
	<version>2.1.2</version>
</dependency>
```

## Usage

With file hierarchy

```
\---fileHierarchy
    |   file1 // content lines: <empty>
    |   file2 // content lines: <empty>
    |
    +---dir1
    |   |   file11 // content line: file11-line1
    |   |
    |   +---dir11
    |   \---dir12
    |           file121 // content line: file121-line1, file121-line2
    |
    \---dir2
        +---dir21
        |       file211 // content lines: file211-line1, file211-line2
        |       file212 // content lines: file212-line1, file212-line2
        |
        \---dir22
            \---dir221
                |   file2211 // content lines: file2211-line1, file2211-line2, file2211-line3
                |
                \---dir2211
                    |   file22111 // content lines: file22111-line1, file22111-line2, file22111-line3, file22111-line4
                    |   file22112 // content lines: file22112-line1, file22112-line2, file22112-line3, file22112-line4
                    |
                    \---dir22111
                        \---dir221111

```
assertions are correct
```java
fileHierarchyAssert.isNotEmpty();
fileHierarchyAssert.hasRootDirWithName("fileHierarchy");
fileHierarchyAssert.hasCountOfSubdirs(10);
fileHierarchyAssert.hasCountOfSubdirs(2, "dir1");
fileHierarchyAssert.hasCountOfFiles(9);
fileHierarchyAssert.hasCountOfFiles(3, "dir2", "dir22", "dir221");
fileHierarchyAssert.hasCountOfFilesAndDirs(20);
fileHierarchyAssert.hasCountOfFilesAndSubdirs(19);
fileHierarchyAssert.hasCountOfFilesAndDirs(1, "dir1", "dir11");
fileHierarchyAssert.hasCountOfFilesAndSubdirs(7, "dir2", "dir22");
fileHierarchyAssert.containsSubdir("dir221", "dir2", "dir22");
fileHierarchyAssert.containsFile("file11", "dir1");
fileHierarchyAssert.containsFileWithContent("file212", Arrays.asList("file212-line1", "file212-line2"),
	"dir2", "dir21");
```
