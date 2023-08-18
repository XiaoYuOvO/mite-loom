package net.fabricmc.loom.configuration.providers.minecraft;

import net.fabricmc.loom.LoomGradleExtension;
import net.fabricmc.loom.util.download.DownloadBuilder;

import org.gradle.api.Project;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Map;
import java.util.function.Function;

public class MITEMetadataProvider extends MinecraftMetadataProvider{
	private final Options options;
	private final Project project;
	private final String miteVersion;
	private final File miteSourceFile;
	private MinecraftVersionMeta miteMetadata;

	public MITEMetadataProvider(Project project,String miteVersion, Options options, Function<String, DownloadBuilder> download) {
		super(options, download);
		this.options = options;
		this.project = project;
		this.miteVersion = miteVersion;
		File workingDir = new File(getExtension().getFiles().getUserCache(),miteVersion);
		this.miteSourceFile = new File(workingDir, miteVersion + ".jar");
	}

	protected LoomGradleExtension getExtension() {
		return LoomGradleExtension.get(project);
	}

	public File getMiteSourceFile() {
		return miteSourceFile;
	}

	@Override
	public MinecraftVersionMeta getVersionMeta() {
		if (miteMetadata == null){
			MinecraftVersionMeta versionMeta = super.getVersionMeta();
			Map<String, MinecraftVersionMeta.Download> downloads = versionMeta.downloads();
			MinecraftVersionMeta.Download value = new MinecraftVersionMeta.Download(miteSourceFile.toString(),
					"",
					-1,
					miteSourceFile.toURI().toString());
			downloads.put("client", value);
			downloads.put("server", value);
			miteMetadata = new MinecraftVersionMeta(versionMeta.arguments(),
					versionMeta.assetIndex(),
					versionMeta.assets(),
					versionMeta.complianceLevel(),
					downloads,
					miteVersion,
					versionMeta.libraries(),
					versionMeta.logging(),
					versionMeta.mainClass(),
					versionMeta.minimumLauncherVersion(),
					versionMeta.releaseTime(),
					versionMeta.time(),
					versionMeta.type());
		}
		return miteMetadata;
	}
}
