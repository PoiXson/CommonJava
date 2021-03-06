package com.poixson.plugins.loaders;

import java.io.File;

import com.poixson.exceptions.RequiredArgumentException;
import com.poixson.plugins.xJavaPlugin;
import com.poixson.plugins.xPluginManager;
import com.poixson.plugins.xPluginYML;
import com.poixson.utils.Utils;


// load a plugin from jar file
public class xPluginLoader_File<T extends xJavaPlugin> extends xPluginLoader<T> {



	public xPluginLoader_File(final xPluginManager<T> manager) {
		super(manager);
	}



	public T LoadJar(final String fileStr) {
		if (Utils.isEmpty(fileStr)) throw new RequiredArgumentException("fileStr");
		return
			this.LoadJar(
				new File(fileStr)
			);
	}
	public T LoadJar(final File file) {
		if (file == null)   throw new RequiredArgumentException("file");
		if (!file.exists()) throw new RuntimeException("Plugin file not found: "+file.getPath());
		// load plugin.yml file from jar
		final xPluginYML yml = this.LoadPluginYML(file);
		if (yml == null)
			return null;
		// load class from jar
		final T plugin =
			this.LoadPluginClass(file, yml);
		return plugin;
	}



}
