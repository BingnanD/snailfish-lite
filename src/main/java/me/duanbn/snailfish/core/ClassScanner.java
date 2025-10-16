package me.duanbn.snailfish.core;

import java.io.File;
import java.io.FileFilter;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import me.duanbn.snailfish.util.collection.Lists;

/**
 * Class扫描工具. 可以扫描某个包下的所有Class信息.
 *
 * @author bingnan.dbn
 */
public class ClassScanner {

	private static final ClassLoader CLASSLOADER = Thread.currentThread().getContextClassLoader();

	public static List<Class<?>> doScan(String scanPackage) {

		List<Class<?>> result = Lists.newArrayList();

		try {
			String pkgDirName = scanPackage.replace(".", "/");
			Enumeration<URL> dirs = CLASSLOADER.getResources(pkgDirName);
			URL url = null;
			while (dirs.hasMoreElements()) {
				url = dirs.nextElement();
				String protocol = url.getProtocol();
				if (protocol.equals("file")) {
					String filePath = URLDecoder.decode(url.getFile(), "utf-8");
					addClassesByFile(result, scanPackage, filePath);
				} else if (protocol.equals("jar")) {
					JarFile jar = null;
					jar = ((JarURLConnection) url.openConnection()).getJarFile();
					Enumeration<JarEntry> entries = jar.entries();
					while (entries.hasMoreElements()) {
						JarEntry entry = entries.nextElement();
						String name = entry.getName();
						if (name.charAt(0) == '/') {
							name = name.substring(1);
						}

						if (!name.startsWith(pkgDirName)) {
							continue;
						}

						if (name.endsWith(".class") && !entry.isDirectory()) {
							String className = name.substring(scanPackage.length() + 1, name.length() - 6).replace("/",
									".");
							Class<?> scanClass = CLASSLOADER.loadClass(scanPackage + "." + className);

							result.add(scanClass);
						}
					}
				}
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		return result;
	}

	private static void addClassesByFile(List<Class<?>> result, String packageName, String packagePath)
			throws ClassNotFoundException {
		File dir = new File(packagePath);
		if (!dir.exists() || !dir.isDirectory()) {
			return;
		}

		File[] dirfiles = dir.listFiles(new FileFilter() {
			@Override
			public boolean accept(File file) {
				return file.isDirectory() || file.getName().endsWith(".class");
			}
		});

		for (File file : dirfiles) {
			if (file.isDirectory()) {
				addClassesByFile(result, packageName + "." + file.getName(), file.getAbsolutePath());
			} else {
				String className = file.getName().substring(0, file.getName().length() - 6);
				Class<?> scanClass = CLASSLOADER.loadClass(packageName + "." + className);
				result.add(scanClass);
			}
		}
	}
}
