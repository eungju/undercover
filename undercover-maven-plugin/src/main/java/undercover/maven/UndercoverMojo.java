package undercover.maven;

import java.io.File;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;

import org.apache.maven.artifact.handler.ArtifactHandler;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.project.MavenProject;

public abstract class UndercoverMojo extends AbstractMojo {
    /**
     * Maven project.
     * 
     * @parameter expression="${project}"
     * @required
     * @readonly
     */
    protected MavenProject project;

    /**
     * Plugin classpath.
     * 
     * @parameter expression="${plugin.artifacts}"
     * @required
     * @readonly
     */
    protected List pluginClasspath;

    /**
     * Location to store Undercover generated resources.
     * 
     * @parameter expression="${undercover.outputDirectory}" default-value="${project.build.directory}/generated-classes/undercover"
     */
    protected File outputDirectory;

    protected void checkParameters() throws MojoExecutionException, MojoFailureException
    {
    }

    public final void execute() throws MojoExecutionException, MojoFailureException
    {
        final ArtifactHandler artifactHandler = project.getArtifact().getArtifactHandler();
        if ( !"java".equals( artifactHandler.getLanguage() ) )
        {
            getLog().info( "Not executing Undercover, as the project is not a Java classpath-capable package" );
            return;
        }

        checkParameters();
        doExecute();
    }

    protected abstract void doExecute() throws MojoExecutionException, MojoFailureException;
}
