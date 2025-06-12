# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [1.1.0] - 2024-06-12

### Added
- Enhanced logging system with colorful and professional formatting
- Box-style initialization messages
- Detailed progress tracking with timestamps
- New log types: SUCCESS, PERFORMANCE, SECURITY
- Section headers for better log organization
- Pre-initialization logging in onLoad()

### Changed
- Improved startup and shutdown messages
- Enhanced chunk operation logging
- Better exception handling with proper stack traces
- More consistent formatting across all log types
- Updated Java version requirement to 17

### Fixed
- Fixed compilation issues with Java version compatibility
- Fixed method signature mismatch in ChunkGenerator
- Improved error handling in logging system

### Security
- Added proper thread safety in logging operations
- Enhanced error handling and reporting

## [1.0.0] - 2024-06-12

### Added
- Initial release
- Basic chunk generation functionality
- Command system
- Configuration system
- Version compatibility (1.16.5 - 1.21.5)
- Progress tracking
- Basic logging system 