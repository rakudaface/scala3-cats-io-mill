import mill._
import mill.scalalib._

import $ivy.`com.lihaoyi::mill-contrib-bloop:$MILL_VERSION`
import $ivy.`com.lihaoyi::mill-contrib-docker:$MILL_VERSION`
import $ivy.`com.lihaoyi::mill-contrib-versionfile:`
import $ivy.`com.goyeau::mill-git::0.2.5`

import mill.contrib.versionfile.VersionFileModule
import mill.contrib.docker.DockerModule

object versionFile extends VersionFileModule

trait ScalaVersionSbtModule extends SbtModule {
  def scalaVersion = "3.3.0"
}

trait ApplicationModule extends ScalaVersionSbtModule with DockerModule { self =>

  import scala.util.Properties
  import com.goyeau.mill.git.GitVersionModule

  object docker extends DockerConfig {

    private val getGitCommitId = GitVersionModule.version(hashLength=40)

    private val getRepositoryPrefix = () => Properties.envOrElse("CONTAINER_REGISTRY", "ghcr.io/rakudaface")

    override def executable = "nerdctl"

    override def pullBaseImage = false

    override def tags = List(
      s"${this.getRepositoryPrefix()}/${self.artifactName()}:${this.getGitCommitId()}",
      s"${this.getRepositoryPrefix()}/${self.artifactName()}:latest"
    )
  }

}

object base extends ScalaVersionSbtModule {

  override def ivyDeps = Agg(
    ivy"org.typelevel::cats-effect::3.5.1",
    ivy"org.typelevel::cats-effect-kernel::3.5.1",
    ivy"org.typelevel::cats-effect-std::3.5.1"
  )

  object test extends ScalaTests with TestModule.Munit {
    override def ivyDeps = Agg(ivy"org.typelevel::munit-cats-effect-3::1.0.7")
  }

}

object app extends ApplicationModule {
  override def moduleDeps = Seq(base)
}
