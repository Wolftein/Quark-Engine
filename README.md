[![License](https://img.shields.io/badge/license-APACHE-blue.svg)](https://github.com/Wolftein/Quark-Engine/blob/master/LICENSE)
![Size](https://reposs.herokuapp.com/?path=Wolftein/Quark-Engine)
[![Build Status](https://travis-ci.org/Wolftein/Quark-Engine.svg?branch=master)](https://travis-ci.org/Wolftein/Quark-Engine)
 
## About

Quark is a cross-platform java framework based on OpenGL that works on desktop and browser.

## Features
  * Extend documentation for ease development
  * Deploy games on Windows, Linux and Mac
  * Deploy games on Browser ([TeaVM](https://github.com/konsoletyper/teavm))
  * Multi-threading support to keep your games running smooth
  * Support for legacy and modern OpenGL (OpenGL 2.1-3.3, OpenGL ES 2.0-3.2) with forward compatibility
  * Support for .PNG, .DDS, .OGG, .WAV file format
  * Support for .FNT format
  * Support for custom binary shader format


## Get started

Currently the framework isn't in maven, so the easiest way to start using it is using [jitpack](http://jipack.io).

	allprojects {
		repositories {
            jcenter()
            maven {
                url "https://jitpack.io"
            }
            maven {
                url "http://oss.sonatype.org/content/groups/public/"
            }
            maven {
                url 'https://oss.sonatype.org/content/repositories/snapshots'
            }
            mavenCentral();
		}
	}
	
    compile 'com.github.Wolftein:Quark-Engine:-SNAPSHOT'

## License

    Copyright (c) 2014-2016 Agustin L. Alvarez <wolftein1@gmail.com>

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
