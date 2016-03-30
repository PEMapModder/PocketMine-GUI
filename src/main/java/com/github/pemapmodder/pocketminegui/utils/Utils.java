package com.github.pemapmodder.pocketminegui.utils;

/*
 * This file is part of PocketMine-GUI.
 *
 * PocketMine-GUI is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * PocketMine-GUI is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with PocketMine-GUI.  If not, see <http://www.gnu.org/licenses/>.
 */

import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorInputStream;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import static com.github.pemapmodder.pocketminegui.utils.Utils.OperatingSystem.LINUX;
import static com.github.pemapmodder.pocketminegui.utils.Utils.OperatingSystem.MAC;
import static com.github.pemapmodder.pocketminegui.utils.Utils.OperatingSystem.WINDOWS;

public class Utils{
	private static OperatingSystem os = null;

	public static interface InstallProgressReporter{
		public void report(double fraction);
		public void completed(File result);
		public void errored();
	}

	public static File installPHP(File home, InstallProgressReporter progress){
		if(getOS() == WINDOWS){
			return installWindowsPHP(home, progress);
		}else if(getOS() == MAC){
			return installMacPHP(home, progress);
		}else{
			return installLinuxPHP(home, progress);
		}
	}

	private static File installLinuxPHP(File home, InstallProgressReporter progress){
		progress.report(0.0);
		File bin = new File(home, ".pmgui_tmp_pm_linux_installer");
		bin.mkdirs();
		try{
			InputStream get = new URL("http", "get.pocketmine.net", "").openStream();
			Process bash = new ProcessBuilder("bash -s - -v development")
					.directory(bin)
					.redirectOutput(ProcessBuilder.Redirect.INHERIT)
					.redirectError(ProcessBuilder.Redirect.INHERIT)
					.start();
			progress.report(0.25);
			byte[] bytes = IOUtils.toByteArray(get);
			progress.report(0.5);
			bash.getOutputStream().write(bytes);
			int result = bash.waitFor();
			progress.report(0.75);
			if(result == 0){
				File out = new File(bin, "bin");
				FileUtils.copyDirectory(out, new File(home, "bin"));
				FileUtils.deleteDirectory(bin);
				File output = new File(out, "php7/bin/php");
				progress.completed(output);
				return output;
			}else{
				FileUtils.deleteDirectory(bin);
				return null;
			}
		}catch(IOException | InterruptedException e){
			e.printStackTrace();
			try{
				FileUtils.deleteDirectory(bin);
			}catch(IOException e1){
			}
			return null;
		}
	}

	private static File installMacPHP(File home, InstallProgressReporter progress){
		return installLinuxPHP(home, progress);
	}

	private static File installWindowsPHP(File home, InstallProgressReporter progress){
		progress.report(0.0);
		try{
			String link = System.getProperty("os.arch").contains("64") ? "https://bintray.com/artifact/download/pocketmine/PocketMine/PHP_7.0.3_x64_Windows.tar.gz" : "https://bintray.com/artifact/download/pocketmine/PocketMine/PHP_7.0.3_x86_Windows.tar.gz";
			URL url = new URL(link);
			InputStream gz = url.openStream();
			GzipCompressorInputStream tar = new GzipCompressorInputStream(gz);
			TarArchiveInputStream is = new TarArchiveInputStream(tar);
			TarArchiveEntry entry;
			while((entry = is.getNextTarEntry()) != null){
				if(!entry.isDirectory()){
					String name = entry.getName();
					byte[] buffer = new byte[(int) entry.getSize()];
					IOUtils.read(is, buffer);
					File real = new File(home, name);
					real.getParentFile().mkdirs();
					IOUtils.write(buffer, new FileOutputStream(real));
				}
			}
			File output = new File(home, "bin/php/php.exe");
			progress.completed(output);
			return output;
		}catch(IOException e){
			e.printStackTrace();
			progress.errored();
			return null;
		}
	}

	public static OperatingSystem getOS(){
		if(os != null){
			return os;
		}
		String osName = System.getProperty("os.name").toLowerCase();
		if(osName.contains("win")){
			return os = WINDOWS;
		}
		if(osName.contains("mac")){
			return os = MAC;
		}
		return os = LINUX;
	}

	public static boolean validatePhpBinaries(File phpBinaries){
		String output = exec(phpBinaries.getAbsolutePath(), "-v");
		if(output != null && output.startsWith("PHP ") && output.contains("The PHP Group") && output.contains("(cli)")){
			String version = output.substring(4, output.indexOf(" (cli)"));
			output = exec(phpBinaries.getAbsolutePath(), "-r",
					"echo (extension_loaded(\"pthreads\") and extension_loaded(\"yaml\")) ? \"ok\" : \"ng\";");
			return "ok".equals(output);
		}
		return false;
	}

	public static String exec(String... cmdLine){
		try{
			Process process = new ProcessBuilder(cmdLine).start();
			process.waitFor();
			return new String(IOUtils.toByteArray(process.getInputStream()));
		}catch(IOException | InterruptedException e){
			e.printStackTrace();
		}
		return null;
	}

	public enum OperatingSystem{
		WINDOWS,
		MAC,
		LINUX
	}
}
