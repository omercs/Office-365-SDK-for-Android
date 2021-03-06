package com.microsoft.office365.test.integration.tests;

import java.util.List;
import java.util.UUID;

import com.microsoft.office365.Constants;
import com.microsoft.office365.files.FileClient;
import com.microsoft.office365.files.FileSystemItem;
import com.microsoft.office365.test.integration.ApplicationContext;
import com.microsoft.office365.test.integration.framework.TestCase;
import com.microsoft.office365.test.integration.framework.TestGroup;
import com.microsoft.office365.test.integration.framework.TestResult;
import com.microsoft.office365.test.integration.framework.TestStatus;

public class FilesTests extends TestGroup {
	public FilesTests() {
		super("Sharepoint Files tests");

		this.addTest(canGetFilesFromDefaultLibrary("Check existing file"));
		this.addTest(canGetFilesFromSpecificLibrary("Check from a specific library"));
		this.addTest(canGetFilePropertiesFromGivenLibAndPath("Get props from lib and path"));
		this.addTest(canGetSpecificProperty("Get specific property from path"));
		this.addTest(canGetSpecificPropertyFromLib("Get specific property from lib and path"));
		this.addTest(canGetFileFromDefaultLib("Can get file from default lib"));
		// TODO:Review
		// this.addTest(canGetFileFromLibAndPath("Can get file from lib and path"));
		this.addTest(canCreateFolderInDefaultDocLib("Can create folder in default doc lib"));
		this.addTest(canCreateFolderInLibAndFolder("Can create folder inside lib"));
		this.addTest(canCreateFilesInDefaultDocLib("Can create file in default doc lib"));
		this.addTest(canCreateFilesInLibAndPath("Can create file in specific lib"));
		this.addTest(canCreateFilesWithContentInDefaultLib("Can create files with content default doc lib"));
		this.addTest(canCreateFilesWithContentInLibAndPath("Can create files with content in lib and path"));
		this.addTest(canDeleteFileInDefaultLib("Can delete file in default lib"));
		this.addTest(canDeleteFileInLibAndFolder("Can delete file in lib and folder"));
		this.addTest(canGetChildrenFolderFromPath("Can get children from path"));
		this.addTest(canMoveFile("Can move file"));
		this.addTest(canCopyFile("Can copy file"));
	}

	private TestCase canCopyFile(String name) {
		TestCase test = new TestCase() {

			@Override
			public TestResult executeTest() {
				try {
					TestResult result = new TestResult();
					result.setStatus(TestStatus.Passed);
					result.setTestCase(this);

					FileClient client = ApplicationContext.getFileClient();
					String fileName = UUID.randomUUID().toString() + ".txt";
					FileSystemItem targetFolder = client.createFolder(
							UUID.randomUUID().toString()).get();
					FileSystemItem fileToCopy = client.createFile(fileName)
							.get();

					String sourcePath = fileToCopy.getName();
					String destinationPath = targetFolder.getName() + "/"
							+ fileToCopy.getName();
					client.copy(sourcePath, destinationPath, true).get();

					client.getFileSystemItem(destinationPath).get();

					return result;
				} catch (Exception e) {
					return createResultFromException(e);
				}
			}
		};
		test.setName(name);
		return test;
	}

	private TestCase canMoveFile(String name) {
		TestCase test = new TestCase() {

			@Override
			public TestResult executeTest() {
				try {
					TestResult result = new TestResult();
					result.setStatus(TestStatus.Passed);
					result.setTestCase(this);

					FileClient client = ApplicationContext.getFileClient();
					String fileName = UUID.randomUUID().toString() + ".txt";
					FileSystemItem targetFolder = client.createFolder(
							UUID.randomUUID().toString()).get();
					FileSystemItem fileToCopy = client.createFile(fileName)
							.get();

					String sourcePath = fileToCopy.getName();
					String destinationPath = targetFolder.getName() + "/"
							+ fileToCopy.getName();
					client.move(sourcePath, destinationPath, true).get();

					client.getFileSystemItem(destinationPath).get();

					return result;
				} catch (Exception e) {
					return createResultFromException(e);
				}
			}
		};
		test.setName(name);
		return test;
	}

	private TestCase canGetChildrenFolderFromPath(String name) {
		TestCase test = new TestCase() {

			@Override
			public TestResult executeTest() {
				try {
					TestResult result = new TestResult();
					result.setStatus(TestStatus.Passed);
					result.setTestCase(this);

					FileClient client = ApplicationContext.getFileClient();
					String lib = ApplicationContext.getTestListName();
					List<FileSystemItem> fileInfo = client.getFileSystemItems(
							"", lib).get();
					if (fileInfo == null) {
						throw new Exception("Cannot list children folders");
					}

					return result;
				} catch (Exception e) {
					return createResultFromException(e);
				}
			}
		};
		test.setName(name);
		return test;
	}

	private TestCase canDeleteFileInLibAndFolder(String name) {
		TestCase test = new TestCase() {

			@Override
			public TestResult executeTest() {
				try {
					TestResult result = new TestResult();
					result.setStatus(TestStatus.Passed);
					result.setTestCase(this);

					FileClient client = ApplicationContext.getFileClient();
					String someFile = UUID.randomUUID().toString() + ".foo";
					String lib = ApplicationContext.getTestListName();
					FileSystemItem fileInfo = client.createFile(someFile, lib)
							.get();
					if (fileInfo == null) {
						throw new Exception(
								"Cannot create the file we want to erase");
					}
					client.delete(fileInfo.getName(), lib);

					return result;
				} catch (Exception e) {
					return createResultFromException(e);
				}
			}
		};
		test.setName(name);
		return test;
	}

	private TestCase canDeleteFileInDefaultLib(String name) {
		TestCase test = new TestCase() {

			@Override
			public TestResult executeTest() {
				try {
					TestResult result = new TestResult();
					result.setStatus(TestStatus.Passed);
					result.setTestCase(this);

					FileClient client = ApplicationContext.getFileClient();
					String someFile = UUID.randomUUID().toString() + ".foo";
					FileSystemItem fileInfo = client.createFile(someFile).get();
					if (fileInfo == null) {
						throw new Exception(
								"Cannot create the file we want to erase");
					}

					client.delete(fileInfo.getName());

					return result;
				} catch (Exception e) {
					return createResultFromException(e);
				}
			}
		};
		test.setName(name);
		return test;
	}

	private TestCase canCreateFilesWithContentInDefaultLib(String name) {
		TestCase test = new TestCase() {

			@Override
			public TestResult executeTest() {
				try {
					TestResult result = new TestResult();
					result.setStatus(TestStatus.Passed);
					result.setTestCase(this);

					FileClient client = ApplicationContext.getFileClient();
					String someFile = UUID.randomUUID().toString() + ".foo";
					byte[] content = "some content"
							.getBytes(Constants.UTF8_NAME);
					FileSystemItem fileInfo = client.createFile(someFile, true,
							content).get();
					if (fileInfo == null) {
						throw new Exception("Expected folder information");
					}
					return result;
				} catch (Exception e) {
					return createResultFromException(e);
				}
			}
		};
		test.setName(name);
		return test;
	}

	private TestCase canCreateFilesWithContentInLibAndPath(String name) {
		TestCase test = new TestCase() {

			@Override
			public TestResult executeTest() {
				try {
					TestResult result = new TestResult();
					result.setStatus(TestStatus.Passed);
					result.setTestCase(this);

					FileClient client = ApplicationContext.getFileClient();
					String lib = ApplicationContext.getTestListName();
					String folderName = UUID.randomUUID().toString();
					client.createFolder(folderName).get();
					String someFile = folderName + "\\"
							+ UUID.randomUUID().toString() + ".fo1";
					byte[] content = "some content"
							.getBytes(Constants.UTF8_NAME);

					FileSystemItem fileInfo = client.createFile(someFile, lib,
							true, content).get();
					String path = (String) fileInfo.getData("Id");
					FileSystemItem stored = client.getFileSystemItem(path, lib)
							.get();

					if (stored == null) {
						throw new Exception("Expected folder information");
					}
					return result;
				} catch (Exception e) {
					return createResultFromException(e);
				}
			}
		};
		test.setName(name);
		return test;
	}

	private TestCase canCreateFilesInLibAndPath(String name) {
		TestCase test = new TestCase() {

			@Override
			public TestResult executeTest() {
				try {
					TestResult result = new TestResult();
					result.setStatus(TestStatus.Passed);
					result.setTestCase(this);

					FileClient client = ApplicationContext.getFileClient();
					String someFile = UUID.randomUUID().toString() + ".foo";
					String path = ApplicationContext.getTestListName();
					FileSystemItem fileInfo = client.createFile(someFile, path)
							.get();
					if (fileInfo == null) {
						throw new Exception("Expected folder information");
					}
					return result;
				} catch (Exception e) {
					return createResultFromException(e);
				}
			}
		};
		test.setName(name);
		return test;
	}

	private TestCase canCreateFilesInDefaultDocLib(String name) {
		TestCase test = new TestCase() {

			@Override
			public TestResult executeTest() {
				try {
					TestResult result = new TestResult();
					result.setStatus(TestStatus.Passed);
					result.setTestCase(this);

					FileClient client = ApplicationContext.getFileClient();
					String someFile = UUID.randomUUID().toString() + ".foo";
					FileSystemItem fileInfo = client.createFile(someFile).get();
					if (fileInfo == null) {
						throw new Exception("Expected folder information");
					}
					return result;
				} catch (Exception e) {
					return createResultFromException(e);
				}
			}
		};
		test.setName(name);
		return test;
	}

	private TestCase canCreateFolderInLibAndFolder(String name) {
		TestCase test = new TestCase() {

			@Override
			public TestResult executeTest() {
				try {
					TestResult result = new TestResult();
					result.setStatus(TestStatus.Passed);
					result.setTestCase(this);

					FileClient client = ApplicationContext.getFileClient();
					String someFolder = UUID.randomUUID().toString();
					String docLib = ApplicationContext.getTestListName();
					FileSystemItem folderInfo = client.createFolder(someFolder,
							docLib).get();
					if (folderInfo == null) {
						throw new Exception("Expected folder information");
					}

					return result;
				} catch (Exception e) {
					return createResultFromException(e);
				}
			}
		};
		test.setName(name);
		return test;
	}

	private TestCase canCreateFolderInDefaultDocLib(String name) {
		TestCase test = new TestCase() {

			@Override
			public TestResult executeTest() {
				try {
					TestResult result = new TestResult();
					result.setStatus(TestStatus.Passed);
					result.setTestCase(this);

					FileClient client = ApplicationContext.getFileClient();
					String someFolder = UUID.randomUUID().toString();
					FileSystemItem folderInfo = client.createFolder(someFolder)
							.get();
					if (folderInfo == null) {
						throw new Exception("Expected folder information");
					}

					return result;
				} catch (Exception e) {
					return createResultFromException(e);
				}
			}
		};
		test.setName(name);
		return test;
	}

	private TestCase canGetSpecificProperty(String name) {
		TestCase test = new TestCase() {

			@Override
			public TestResult executeTest() {
				try {
					TestResult result = new TestResult();
					result.setStatus(TestStatus.Passed);
					result.setTestCase(this);

					FileClient client = ApplicationContext.getFileClient();

					String path = UUID.randomUUID().toString() + ".txt";

					client.createFile(path).get();

					String propertyName = "Name";
					Object property = client.getProperty(propertyName, path)
							.get();
					if (property == null) {
						throw new Exception("Expected at least one file");
					}

					return result;
				} catch (Exception e) {
					return createResultFromException(e);
				}
			}
		};
		test.setName(name);
		return test;
	}

	private TestCase canGetSpecificPropertyFromLib(String name) {
		TestCase test = new TestCase() {

			@Override
			public TestResult executeTest() {
				try {
					TestResult result = new TestResult();
					result.setStatus(TestStatus.Passed);
					result.setTestCase(this);

					FileClient client = ApplicationContext.getFileClient();
					String library = ApplicationContext.getTestListName();
					String folder = UUID.randomUUID().toString();
					client.createFolder(folder, library).get();

					String propertyName = "Name";
					Object property = client.getProperty(propertyName, folder,
							library).get();
					if (property == null) {
						throw new Exception("Expected at least one file");
					}

					return result;
				} catch (Exception e) {
					return createResultFromException(e);
				}
			}
		};
		test.setName(name);
		return test;
	}

	private TestCase canGetFilesFromDefaultLibrary(String name) {
		TestCase test = new TestCase() {

			@Override
			public TestResult executeTest() {
				try {
					TestResult result = new TestResult();
					result.setStatus(TestStatus.Passed);
					result.setTestCase(this);

					FileClient client = ApplicationContext.getFileClient();
					String fileName = UUID.randomUUID().toString() + ".txt";
					client.createFile(fileName).get();

					List<FileSystemItem> files = client.getFileSystemItems()
							.get();
					if (files.size() == 0) {
						throw new Exception("Expected at least one file");
					}

					return result;
				} catch (Exception e) {
					return createResultFromException(e);
				}
			}
		};

		test.setName(name);
		return test;
	}

	private TestCase canGetFilesFromSpecificLibrary(String name) {

		TestCase test = new TestCase() {

			@Override
			public TestResult executeTest() {
				try {
					TestResult result = new TestResult();
					result.setStatus(TestStatus.Passed);
					result.setTestCase(this);

					FileClient client = ApplicationContext.getFileClient();
					String someLibrary = "TestDocLib";

					client.createFile(UUID.randomUUID().toString() + ".txt",
							someLibrary).get();

					List<FileSystemItem> files = client.getFileSystemItems(
							null, someLibrary).get();
					if (files.size() == 0) {
						throw new Exception("Expected at least one file");
					}

					return result;
				} catch (Exception e) {
					return createResultFromException(e);
				}
			}

		};
		test.setName(name);
		return test;
	}

	private TestCase canGetFilePropertiesFromGivenLibAndPath(String name) {

		TestCase test = new TestCase() {

			@Override
			public TestResult executeTest() {
				try {
					TestResult result = new TestResult();
					result.setStatus(TestStatus.Passed);
					result.setTestCase(this);

					FileClient client = ApplicationContext.getFileClient();
					String folderName = UUID.randomUUID().toString();
					String docLib = ApplicationContext.getTestListName();
					client.createFolder(folderName, docLib).get();

					List<FileSystemItem> files = client.getFileSystemItems(
							folderName, docLib).get();
					if (files == null) {
						throw new Exception("Expected folder information");
					}

					return result;
				} catch (Exception e) {
					return createResultFromException(e);
				}
			}
		};
		test.setName(name);
		return test;
	}

	private TestCase canGetFileFromDefaultLib(String name) {

		TestCase test = new TestCase() {

			@Override
			public TestResult executeTest() {
				try {
					TestResult result = new TestResult();
					result.setStatus(TestStatus.Passed);
					result.setTestCase(this);

					FileClient client = ApplicationContext.getFileClient();
					String fileName = UUID.randomUUID().toString() + ".txt";
					client.createFile(fileName).get();
					byte[] file = client.getFile(fileName).get();
					if (file == null) {
						throw new Exception("Expected at least one file");
					}

					return result;
				} catch (Exception e) {
					return createResultFromException(e);
				}
			}
		};
		test.setName(name);
		return test;
	}

	// private TestCase canGetFileFromLibAndPath(String name) {
	// TestCase test = new TestCase() {
	//
	// @Override
	// public TestResult executeTest() {
	// try {
	// TestResult result = new TestResult();
	// result.setStatus(TestStatus.Passed);
	// result.setTestCase(this);
	// String docLib = "TestDocLib";
	// FileClient client = ApplicationContext.getFileClient();
	// String folder = UUID.randomUUID().toString();
	// client.createFolder(folder, docLib).get();
	// String fileName = UUID.randomUUID().toString() + ".txt";
	// String path = folder + "\\" + fileName;
	//
	// client.createFile(path, docLib).get();
	// byte[] file = client.getFile(path, docLib).get();
	// if (file == null) {
	// throw new Exception("Expected at least one file");
	// }
	//
	// return result;
	// } catch (Exception e) {
	// return createResultFromException(e);
	// }
	// }
	// };
	// test.setName(name);
	// return test;
	// }
}
