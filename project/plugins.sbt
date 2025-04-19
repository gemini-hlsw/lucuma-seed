resolvers in Global += Resolver.sonatypeRepo("public")
resolvers in Global += Resolver.sonatypeRepo("snapshots")

addSbtPlugin("edu.gemini"   % "sbt-lucuma-app" % "0.12.9")
addSbtPlugin("com.eed3si9n" % "sbt-buildinfo"  % "0.11.0")
