package com.poixson.utils;

import java.io.Closeable;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import com.poixson.utils.exceptions.RequiredArgumentException;


public final class DirsFiles {
	private DirsFiles() {}



	public static void init() {
		Keeper.add(new DirsFiles());
	}



	// get current working directory
	public static String cwd() {
		try {
			return (new File("."))
					.getCanonicalPath()
					.toString();
		} catch (IOException ignore) {}
		return null;
	}



	/**
	 * List contents of a directory.
	 * @param dir The directory path to query.
	 * @param extensions File extensions to include, filtering out all others.
	 * To list all contents, set this to null.
	 * @return
	 */
	public static File[] listContents(final File dir, final String[] extensions) {
		if (dir == null) throw new RequiredArgumentException("dir");
		if (!dir.isDirectory()) return null;
		return dir.listFiles(
			new FileFilter() {
				private String[] exts;
				public FileFilter init(final String[] extens) {
					this.exts = extens;
					return this;
				}
				@Override
				public boolean accept(File path) {
					if (this.exts == null)
						return true;
					final String pathStr = path.toString();
					for (final String ext : this.exts){
						if (pathStr.endsWith(ext))
							return true;
					}
					return false;
				}
			}.init(extensions)
		);
	}
	public static File[] listContents(final File dir, final String extension) {
		return listContents(dir, new String[] {extension});
	}
	public static File[] listContents(final File dir) {
		return listContents(dir, (String[]) null);
	}



/*
//TODO: does this work?
	// add lib to paths
	public static void addLibraryPath(final String libDir) {
		if (utils.isEmpty(libDir)) throw new RequiredArgumentException("libDir");
		// get lib path
		final File file = new File(libDir);
		if (!file.exists() || !file.isDirectory()) {
			log().warning("Library path not found: "+libDir);
			return;
		}
		final String libPath = file.getAbsolutePath();
		if (utils.isEmpty(libPath)) return;
		// get current paths
		final String currentPaths = System.getProperty("java.library.path");
		if (currentPaths == null) return;
//		pxnLog.get().debug("Adding lib path: "+libDir);
		// set library paths
		if (currentPaths.isEmpty()) {
			System.setProperty("java.library.path", libPath);
		} else {
			if (currentPaths.contains(libPath)) return;
			System.setProperty("java.library.path", currentPaths+( currentPaths.contains(";") ? ";" : ":" )+libPath);
		}
		// force library paths to refresh
		try {
			final Field fieldSysPath = ClassLoader.class.getDeclaredField("sys_paths");
			fieldSysPath.setAccessible(true);
			fieldSysPath.set(null, null);
		} catch (SecurityException ignore) {
		} catch (NoSuchFieldException ignore) {
		} catch (IllegalArgumentException ignore) {
		} catch (IllegalAccessException ignore) {
		}
	}
*/



	// open file
	public static InputStream OpenFile(final File file) {
		if (file == null)   return null;
		if (!file.exists()) return null;
		try {
			return new FileInputStream(file);
		} catch (FileNotFoundException ignore) {}
		return null;
	}
	// load resource
	public static InputStream OpenResource(final String fileStr) {
		if (Utils.isEmpty(fileStr)) return null;
		try {
			return DirsFiles.class.getResourceAsStream(
				StringUtils.ForceStarts("/", fileStr)
			);
		} catch(Exception ignore) {}
		return null;
	}
	// load yml from jar reference
	public static InputStream OpenResource(final Class<? extends Object> clss, final String fileName) {
		if (clss == null)            throw new RequiredArgumentException("clss");
		if (Utils.isEmpty(fileName)) throw new RequiredArgumentException("fileName");
		final InputStream in = clss.getResourceAsStream(fileName);
		return in;
	}
	// load yml from jar
	public static InputJar OpenJarResource(final File jarFile, final String fileName) {
		if (jarFile == null)         throw new RequiredArgumentException("jarFile");
		if (Utils.isEmpty(fileName)) throw new RequiredArgumentException("fileName");
		try {
			final JarFile jar = new JarFile(jarFile);
			final JarEntry entry = jar.getJarEntry(fileName);
			if (entry == null) {
				Utils.safeClose(jar);
				return null;
			}
			final InputStream in = jar.getInputStream(entry);
			if (in == null) {
				Utils.safeClose(jar);
				return null;
			}
			return new InputJar(jar, in);
		} catch (IOException ignore) {}
		return null;
	}
	public static class InputJar implements Closeable {
		public final JarFile jar;
		public final InputStream fileInput;
		public InputJar(final JarFile jar, final InputStream fileInput) {
			this.jar       = jar;
			this.fileInput = fileInput;
		}
		@Override
		public void finalize() {
			this.close();
		}
		@Override
		public void close() {
			Utils.safeClose(this.jar);
			Utils.safeClose(this.fileInput);
		}
	}



// TODO: these functions can have inconsistent results. a better class will be needed



	// build path+file
	public static String buildFilePath(final String filePath,
			final String fileName, final String extension) {
		if (Utils.isEmpty(fileName)) throw new RequiredArgumentException("fileName");
		// file extension
		final String ext;
		if (Utils.isEmpty(extension)) {
			ext = ".yml";
		} else
		if (!extension.startsWith(".")) {
			ext = "."+extension;
		} else {
			ext = extension;
		}
		final String fileStr = StringUtils.ForceEnds(ext, fileName);
		if (filePath == null || filePath.isEmpty())
			return fileStr;
		final boolean a = (filePath.endsWith("/")  || filePath.endsWith("\\"));
		final boolean b = (fileStr.startsWith("/") || fileStr.startsWith("\\"));
		if (a && b) return filePath + fileStr.substring(1);
		if (a || b) return filePath + fileStr;
		return (new StringBuilder())
			.append(filePath)
			.append(File.separator)
			.append(fileStr)
			.toString();
	}
	public static String buildFilePath(final File path, final File file) {
		if (file == null) throw new RequiredArgumentException("file");
		final String fileStr = file.getPath();
		if (path == null) {
			return fileStr;
		}
		final String pathStr = path.getPath();
		return (new StringBuilder())
			.append(pathStr)
			.append(pathStr.endsWith("/") ? "" : "/")
			.append(fileStr)
			.toString();
	}



	public static String mergePaths(final String...strings) {
		final StringBuilder merged = new StringBuilder();
		for (String path : strings) {
			if (Utils.isEmpty(path)) continue;
			if (path.equals(".")) {
				path = cwd();
			}
			path = StringUtils.trims(path, "/", "\\", " ", "\t", "\r", "\n");
			if (Utils.isEmpty(path))
				continue;
			merged
				.append(path)
				.append(File.separatorChar);
		}
		if (merged.length() == 0)
			return null;
		return merged.toString();
	}



//	// logger
//	public static xLog log() {
//		return Utils.log();
//	}



}
