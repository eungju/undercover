package undercover.maven;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.ArtifactUtils;
import org.apache.maven.artifact.factory.ArtifactFactory;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;

import undercover.instrument.OfflineInstrument;
import undercover.instrument.filter.GlobFilter;
import undercover.runtime.UndercoverSettings;

/**
 * Offline class instrumentor.
 *
 * @goal instrument
 * @requiresDependencyResolution test
 */
public class InstrumentMojo extends UndercoverMojo {
    /**
     * Specifies the instrumentation paths to use.
     *
     * @parameter
     */
    protected File[] instrumentationPaths;

    /**
     * Location to store class coverage metadata.
     *
     * @parameter expression="${undercover.metaDataFile}"
     */
    protected File metaDataFile;

    /**
     * Location to store class coverage data.
     *
     * @parameter expression="${undercover.coverageDataFile}"
     */
    protected File coverageDataFile;
    
    /**
     * Inclusion class name patterns.
     * 
     * @parameter
     */
    protected String[] includes;
    
    /**
     * Exclusion class name patterns.
     * 
     * @parameter
     */
    protected String[] excludes;
    
    /**
     * Artifact factory.
     *
     * @component
     */
    private ArtifactFactory factory;

    protected void checkParameters() throws MojoExecutionException, MojoFailureException {
		super.checkParameters();

		if (instrumentationPaths == null) {
			List<File> paths = new ArrayList<File>();
        	for (String each : Arrays.asList(project.getBuild().getOutputDirectory())) {
            	File file = new File(each);
            	if (file.exists()) {
            		paths.add(file);
            	}
        	}
			getLog().info("Instrumentation paths: " + paths);
			instrumentationPaths = paths.toArray(new File[paths.size()]);
		}
		
		if (metaDataFile == null) {
			metaDataFile = new File(outputDirectory, "undercover.md");
		}
		
		if (coverageDataFile == null) {
			 coverageDataFile = new File(outputDirectory, "undercover.cd");
		}
		
		if (includes == null) {
			includes = new String[0];
		}
		if (excludes == null) {
			excludes = new String[0];
		}
	}

    protected void doExecute() throws MojoExecutionException {
    	try {
	    	OfflineInstrument instrument = new OfflineInstrument();
	    	instrument.setInstrumentPaths(Arrays.asList(instrumentationPaths));
	    	instrument.setOutputDirectory(outputDirectory);
	    	instrument.setMetaDataFile(metaDataFile);
    		instrument.setFilter(new GlobFilter(includes, excludes));
	    	instrument.fullcopy();

			UndercoverSettings settings = new UndercoverSettings();
			settings.setCoverageSaveOnExit(true);
			settings.setCoverageFile(coverageDataFile);
			settings.save(new File(outputDirectory, "classes/undercover.properties"));
    	} catch (Exception e) {
    		throw new MojoExecutionException("Failed to instrument", e);
    	}
    	addUndercoverDependenciesToTestClasspath();
	}

    /**
     * Add Undercover dependency to project test classpath. When tests are executed, Undercover runtime dependency is required.
     *
     * @throws MojoExecutionException if Undercover dependency could not be added
     */
    private void addUndercoverDependenciesToTestClasspath() throws MojoExecutionException {
        // look for Undercover dependency in this plugin classpath
		final Map<String, Artifact> pluginArtifactMap = ArtifactUtils.artifactMapByVersionlessId(pluginClasspath);
		Artifact artifact = pluginArtifactMap.get("undercover:undercover");

        if (artifact == null) {
			throw new MojoExecutionException("Failed to find 'undercover' artifact in plugin dependencies");
		}

		// set the dependency scope to test
		artifact = artifactScopeToTest(artifact);

		// add to project dependencies
		final Set<Artifact> deps = new HashSet<Artifact>();
		if (project.getDependencyArtifacts() != null) {
			deps.addAll(project.getDependencyArtifacts());
		}
		deps.add(artifact);
		project.setDependencyArtifacts(deps);
    }

    private Artifact artifactScopeToTest(Artifact artifact) {
        return factory.createArtifact(artifact.getGroupId(), artifact.getArtifactId(), artifact.getVersion(), Artifact.SCOPE_TEST, artifact.getType());
    }
}
