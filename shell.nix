{
  system,
  nixpkgs,
}:

let
  buildToolsVersion = "37.0.0";
  buildToolsVersions = [ buildToolsVersion ];
  platformVersions = [ "37" ];

  # Cosmetic node version label the Kotlin/Wasm tooling looks for; must match KOTLIN_NODE_VERSION
  # in build.gradle.kts.
  nodeVersion = "25.0.0";
  nodeDir = "node-v${nodeVersion}-linux-${if system == "aarch64-linux" then "arm64" else "x64"}";

  pkgs = import nixpkgs {
    inherit system;
    config.allowUnfree = true;
    config.android_sdk.accept_license = true;
  };

  jdk = pkgs.jdk21;

  androidSdk = pkgs.androidenv.composeAndroidPackages {
    inherit buildToolsVersions platformVersions;
    includeNDK = false;
    includeEmulator = false;
  };
in
{
  default = pkgs.mkShell {
    packages = [
      jdk
      androidSdk.androidsdk
      pkgs.nodejs
      pkgs.yarn
    ];

    env = {
      ANDROID_HOME = "${androidSdk.androidsdk}/libexec/android-sdk";

      JAVA_HOME = "${jdk}";

      # aapt2 bundled in the AGP Maven artifact is a generic-Linux binary
      # that NixOS cannot run. Override it with the Nix-patched copy.
      GRADLE_OPTS = "-Dorg.gradle.project.android.aapt2FromMavenOverride=${androidSdk.androidsdk}/libexec/android-sdk/build-tools/${buildToolsVersion}/aapt2";

      # NixOS can't run the node binary Kotlin/Wasm would download; build.gradle.kts disables the
      # download when these are set and uses the Nix node symlinked in below instead.
      KOTLIN_NODE_HOME = "${pkgs.nodejs}";
      KOTLIN_NODE_VERSION = nodeVersion;
    };

    shellHook = ''
      cat > "$PWD/local.properties" <<EOF
      sdk.dir=${androidSdk.androidsdk}/libexec/android-sdk
      EOF

      # Make the node dir the Kotlin/Wasm tooling expects resolve to the runnable Nix node.
      gradle_home="''${GRADLE_USER_HOME:-$HOME/.gradle}"
      mkdir -p "$gradle_home/nodejs"
      ln -sfn ${pkgs.nodejs} "$gradle_home/nodejs/${nodeDir}"
    '';
  };
}
