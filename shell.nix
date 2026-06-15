{
  system,
  nixpkgs,
}:

let
  buildToolsVersion = "37.0.0";
  buildToolsVersions = [ buildToolsVersion ];
  platformVersions = [ "36" ];

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
    ];

    env = {
      ANDROID_HOME = "${androidSdk.androidsdk}/libexec/android-sdk";

      JAVA_HOME = "${jdk}";

      # aapt2 bundled in the AGP Maven artifact is a generic-Linux binary
      # that NixOS cannot run. Override it with the Nix-patched copy.
      GRADLE_OPTS = "-Dorg.gradle.project.android.aapt2FromMavenOverride=${androidSdk.androidsdk}/libexec/android-sdk/build-tools/${buildToolsVersion}/aapt2";
    };

    shellHook = ''
      cat > "$PWD/local.properties" <<EOF
      sdk.dir=${androidSdk.androidsdk}/libexec/android-sdk
      EOF
    '';
  };
}
