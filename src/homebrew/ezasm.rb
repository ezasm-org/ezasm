class ezasm < Formula
  desc "An assembly-like programming language for use in education"
  homepage "https://github.com/ezasm-org/EzASM"
  url "https://github.com/ezasm-org/EzASM.git"
  license "MIT"

  depends_on "maven" => :build
  depends_on "openjdk@17"

  def install
    system "mvn", "compile", "assembly:single"
    libexec.install "target/EzASM-*-full.jar"
    bin.write_jar_script libexec/"EzASM-*-full.jar", "ezasm", java_version: "17"
  end

  test do
    system "#{bin}/ezasm", "--version"
  end
end