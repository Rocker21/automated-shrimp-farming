# ShrimpCaring — Automated Shrimp Farming & Monitoring System

A detailed engineering README for an IoT-enabled shrimp-farming monitoring and automation platform.

Repository: automated-shrimp-farming

Project status: Active development

---

## PROJECT OVERVIEW

- ShrimpCaring is an IoT-oriented automated shrimp-farm monitoring and management platform.
- The project combines an Android application, ESP32 controllers, Raspberry Pi services, sensors, actuators, a FastAPI backend, and SQLite data storage.
- The platform is designed to centralize pond information and provide a practical interface for farm monitoring and automation.
- The current implementation includes pond management, aerator management, pH sensor configuration, electrical parameter display, and ESP32-to-Raspberry-Pi communication.
- The architecture is intended to support additional environmental sensors and automated farm functions as the project evolves.
- The Android application provides the user-facing control and monitoring interface.
- The ESP32 provides a hardware control layer for connected relays and farm equipment.
- The Raspberry Pi can host the FastAPI service and coordinate connected devices.
- SQLite provides lightweight persistent storage for pond and operational data.
- The project is intended as an engineering and IoT development platform for smart aquaculture.

---

## PROJECT MOTIVATION

- Shrimp farming involves equipment and environmental conditions that benefit from continuous observation.
- Manual monitoring can make it difficult to maintain consistent records and respond quickly to changing conditions.
- Automation can reduce repetitive manual operations and provide more consistent equipment control.
- A connected architecture allows pond information to be viewed from a centralized application.
- The project therefore combines sensing, control, communication, and software visualization.
- The design emphasizes modularity so individual sensors or actuators can be added without rebuilding the entire platform.
- The system also provides a foundation for future analytics and computer-vision features.
- Camera monitoring can eventually complement sensor data with visual information.
- Historical data can support later trend analysis.
- The overall goal is a practical connected aquaculture management platform.

---

## OBJECTIVES

- Create a centralized application for shrimp-pond management.
- Allow multiple ponds to be represented in the Android application.
- Allow components to be associated with individual ponds.
- Provide an interface for configuring aerators.
- Retrieve aerator information from the Raspberry Pi backend.
- Send control commands to connected ESP32 hardware.
- Provide pH sensor configuration.
- Support storage of pond-specific configuration.
- Display electrical parameters received by the application.
- Provide a base for future automated farm-control algorithms.

---

## CORE FEATURES

- Pond creation and selection.
- Pond detail screens.
- Pond component management.
- Aerator count configuration.
- Aerator status display.
- Aerator control workflow.
- pH sensor configuration.
- pH sensor display workflow.
- Persistent pond configuration using Android preferences.
- Raspberry Pi backend communication.
- ESP32 Wi-Fi communication.
- ESP32 HTTP server.
- ESP32 registration with the Raspberry Pi.
- Four-relay aerator controller support in the current ESP32 implementation.
- Wi-Fi reconnection handling.
- Error reporting when the Raspberry Pi cannot be reached.
- Loading states in the Android interface.
- Last-update display for electrical parameters.
- Foundation for camera monitoring and additional automation.

---

## SYSTEM ARCHITECTURE

- The system is organized around several cooperating layers.
- The Android application is the primary user interface.
- The Raspberry Pi provides a local server and device-management layer.
- The ESP32 acts as a field controller for relays and connected equipment.
- Sensors provide measurements to the embedded or networked system.
- Actuators receive control commands through relay outputs.
- The FastAPI backend exposes REST endpoints to the Android application and ESP32.
- SQLite provides persistent backend storage.
- The camera subsystem can be hosted on the Raspberry Pi.
- The architecture can be extended with cloud services later.

---

## HIGH LEVEL DATA FLOW

- The user opens the ShrimpCaring Android application.
- The application loads locally stored pond configuration.
- The user selects a pond.
- The application displays the components configured for that pond.
- The user can open aerator management.
- The application requests aerator information from the repository.
- The repository communicates with the Raspberry Pi backend.
- The backend reads the relevant device and pond data.
- The Android application receives the result.
- The interface displays the current aerator states.

---

## AERATOR CONTROL FLOW

- The user selects an aerator in the Android application.
- The application marks the aerator as busy while a command is being processed.
- A control request is sent through the repository.
- The backend determines the associated ESP32 device.
- The ESP32 receives the relay command.
- The ESP32 changes the corresponding relay output.
- The relay switches the connected equipment.
- The device state can then be returned to the backend.
- The Android application refreshes the displayed state.
- A failure is reported through the application's error state.

---

## ANDROID APPLICATION

- The Android application is implemented using Kotlin.
- The user interface uses modern declarative Android UI patterns.
- The main navigation state includes HOME.
- The main navigation state includes ADD_POND.
- The main navigation state includes POND_DETAILS.
- The application includes SET_AERATOR_COUNT.
- The application includes AERATORS.
- The application includes SET_PH_CONFIG.
- The application includes PH_SENSOR.
- The main activity controls transitions between these screens.

---

## ANDROID NAVIGATION

- The Home screen displays available ponds.
- The Add Pond screen creates a new pond.
- Selecting a pond opens its detail screen.
- The pond detail screen exposes configured components.
- Selecting Aerators opens aerator management or aerator-count setup.
- Selecting pH Sensor opens the pH sensor display or configuration screen.
- The navigation icon returns to the previous logical screen.
- Pond creation immediately selects the new pond.
- Pond names are persisted in Android preferences.
- Component-specific configuration is persisted with pond-specific keys.

---

## POND MANAGEMENT

- Ponds are represented as application-level objects.
- A pond can have a name.
- A pond can have an aerator configuration.
- A pond can have a pH sensor configuration.
- A pond can store an authentication token.
- A pond can store a template identifier.
- A pond can store a pH virtual-pin identifier.
- The application updates the selected pond after configuration changes.
- The application persists important configuration values.
- The pond model provides the foundation for additional farm components.

---

## AERATOR MANAGEMENT

- The application can configure the number of aerators for a pond.
- The aerator management screen retrieves aerators from the Raspberry Pi backend.
- A loading state is displayed while the request is in progress.
- A server error state is displayed if retrieval fails.
- Each aerator has an identifier.
- Each aerator has a state.
- The current implementation treats state 1 as ON.
- The application tracks aerators that are currently receiving commands.
- This busy state helps prevent confusing UI interactions during a request.
- The backend acts as the bridge between application commands and field devices.

---

## PH SENSOR CONFIGURATION

- The Android application includes a dedicated pH sensor configuration workflow.
- The pH configuration screen accepts a Blynk virtual pin.
- The default example pin in the current UI is V4.
- The user can enter a different virtual pin.
- The application prevents saving an empty pin.
- The selected pin is stored with the pond configuration.
- The pH management screen provides Display and Setup tabs.
- The Display tab presents the current pH value.
- The display also tracks online status.
- The display provides a refresh workflow.

---

## PH SENSOR DATA FLOW

- A pond provides an authentication token for the pH service.
- The pond provides a configured virtual pin.
- The pH screen checks that required configuration exists.
- The application requests the current pH value.
- The result is represented in the UI.
- The screen maintains an online indicator.
- The screen maintains a loading state during refresh.
- A status string communicates the current operation.
- A failed request can be surfaced to the user.
- The configuration is kept specific to the selected pond.

---

## ELECTRICAL PARAMETERS

- The Android application includes an Electrical Parameters section.
- Voltage is displayed in volts.
- Frequency is displayed in hertz.
- Power factor is displayed as a dimensionless value.
- Energy is displayed in kilowatt-hours.
- Current is maintained by the application state.
- Power is maintained by the application state.
- The application records a last-update timestamp.
- The displayed time uses a local HH:mm:ss format.
- The interface shows Updating when a valid timestamp has not yet been received.

---

## RASPBERRY PI BACKEND

- The Raspberry Pi can host the project's FastAPI backend.
- The backend provides REST APIs for farm data.
- The backend provides device registration functionality.
- The backend can store pond information.
- The backend can store device information.
- The backend can expose aerator data.
- The backend can coordinate ESP32 devices.
- SQLite is used as the lightweight database layer.
- The backend provides a central point for Android-to-device communication.
- The architecture can later be deployed to a larger server if required.

---

## FASTAPI ROLE

- FastAPI provides HTTP endpoints for the ShrimpCaring system.
- The API separates mobile UI logic from hardware control.
- The Android application can communicate with the backend through a repository layer.
- The ESP32 can communicate with the backend through HTTP.
- Device registration is exposed through an API endpoint.
- The current ESP32 registration path is /api/devices/register.
- The ESP32 sends its device name during registration.
- The ESP32 sends its local IP address during registration.
- The ESP32 sends its configured pond ID during registration.
- The backend can use these values to associate hardware with a pond.

---

## SQLITE DATABASE

- SQLite provides persistent local storage for backend data.
- The database is appropriate for development and smaller deployments.
- Pond records can be stored in the database.
- Device records can be stored in the database.
- Aerator records can be associated with pond and device information.
- Operational data can be extended with timestamps.
- Sensor history can be added as additional tables.
- The database avoids requiring a separate database server during development.
- Database access should be isolated from UI code.
- Backups should be considered before production deployment.

---

## ESP32 CONTROLLER

- The current ESP32 firmware is designed as an aerator controller.
- The firmware uses Wi-Fi for network communication.
- The firmware uses an HTTP server for control requests.
- The ESP32 exposes a status endpoint.
- The ESP32 exposes a relay-control endpoint.
- The firmware registers itself with the Raspberry Pi.
- The ESP32 supports four relay outputs in the current controller.
- Relay GPIO pins are 25, 26, 27, and 33 in the current firmware.
- The relay outputs use active-low logic.
- The firmware turns all relays off during initialization.

---

## ESP32 STARTUP

- The firmware initializes the relay GPIO pins.
- Each relay is configured as an OUTPUT.
- The initial HIGH level represents OFF for the active-low relay module.
- The firmware explicitly calls the all-relays-off routine.
- The ESP32 connects to Wi-Fi.
- The HTTP routes are registered.
- The HTTP server starts on port 80.
- The ESP32 registers with the Raspberry Pi.
- The firmware prints a ready message through the serial interface.
- The device then enters the main loop.

---

## ESP32 MAIN LOOP

- The main loop services incoming HTTP requests.
- The web server processes commands received from the Raspberry Pi.
- The firmware periodically checks Wi-Fi status.
- Wi-Fi maintenance helps recover from connectivity loss.
- The loop avoids unnecessary blocking operations.
- Relay state remains controlled by the command handlers.
- The device continues operating while network connectivity is available.
- The architecture permits additional sensor polling to be added later.
- The architecture permits telemetry transmission to be added later.
- The loop is the central runtime cycle of the controller.

---

## ESP32 WIFI

- The ESP32 uses Wi-Fi to communicate with the local network.
- The firmware contains a configured network name.
- The firmware contains a configured network password.
- Production deployments should never publish real credentials in a public repository.
- Wi-Fi status is checked during operation.
- The current firmware uses a five-second Wi-Fi check interval.
- The device tracks whether Wi-Fi was previously connected.
- Reconnection logic can restore communication after temporary network loss.
- Network configuration should be moved to a secure provisioning process.
- The existing BLE provisioning workflow provides a foundation for that improvement.

---

## ESP32 DEVICE REGISTRATION

- The ESP32 registers with the Raspberry Pi after Wi-Fi initialization.
- Registration uses an HTTP POST request.
- The registration endpoint is /api/devices/register.
- The request content type is application/json.
- The payload contains device_name.
- The payload contains ip_address.
- The payload contains pond_id.
- The device name identifies the field controller.
- The pond ID associates the controller with a pond.
- The Raspberry Pi response is printed through the serial console.

---

## BLE PROVISIONING

- The ESP32 firmware includes a Bluetooth configuration workflow.
- The BLE device name is ShrimpCaring-ESP32.
- A BLE service is created using the configured service UUID.
- A writable characteristic is provided for the Wi-Fi SSID.
- A writable characteristic is provided for the Wi-Fi password.
- A writable characteristic is provided for the template ID.
- A writable characteristic is provided for the Blynk authentication token.
- A save characteristic receives the SAVE command.
- The ESP32 checks that required configuration values exist before saving.
- This workflow reduces the need to hard-code network configuration into firmware.

---

## BLE CALLBACKS

- The SSID callback receives the configured Wi-Fi network name.
- The password callback receives the configured Wi-Fi password.
- The template callback receives the configured template ID.
- The token callback receives the Blynk authentication token.
- The save callback checks for the SAVE command.
- The save callback verifies that required configuration is present.
- Missing configuration results in an error message.
- A successful configuration can be persisted by the firmware.
- The BLE server reports phone connection events.
- The server restarts advertising after a phone disconnects.

---

## SECURITY

- Public repositories must not contain real Wi-Fi passwords.
- Public repositories must not contain real Blynk authentication tokens.
- Public repositories should not expose private network addresses unnecessarily.
- Credentials should be supplied through secure configuration.
- The BLE provisioning workflow should validate configuration input.
- Production APIs should use authentication.
- Production network traffic should use HTTPS where supported.
- Physical access to controllers should be restricted.
- Relay-control endpoints should not be exposed directly to the public Internet.
- Logs should avoid printing secrets.

---

## CAMERA MONITORING

- The Raspberry Pi can be extended with a camera for visual monitoring.
- A USB webcam can be used with the Raspberry Pi.
- A Raspberry Pi-compatible camera can also be used.
- Camera monitoring can provide visual context alongside sensor readings.
- The camera subsystem can monitor pond areas.
- The camera can monitor equipment areas.
- Motion detection can be added as a future feature.
- Night monitoring can use an appropriate low-light or NoIR camera.
- Recorded footage should be handled with appropriate privacy controls.
- Large camera recordings should not normally be committed to Git.

---

## AUTOMATION CONCEPT

- Automation rules can connect sensor conditions to equipment actions.
- A rule can be expressed as a condition followed by an action.
- The backend can evaluate scheduled tasks.
- The ESP32 can execute relay commands.
- The Android application can provide manual overrides.
- Automation should include safe fallback behavior.
- Sensor failures should not silently produce unsafe outputs.
- Network failures should be handled explicitly.
- Equipment state should be confirmed after commands when possible.
- Automation thresholds should be configurable.

---

## AERATOR AUTOMATION

- Aerators can be controlled through relay outputs.
- A pond can contain multiple aerators.
- Each aerator can have an identifier.
- Each aerator can have a state.
- The application retrieves the configured aerators from the backend.
- The application can initiate a command for an individual aerator.
- The controller maps the command to a relay.
- The relay switches the physical aerator circuit.
- The system can later automate aeration using configured sensor thresholds.
- Any automated threshold should be validated for the specific farming environment.

---

## POTENTIAL SENSORS

- Temperature sensing can be added to monitor water temperature.
- pH sensing can be integrated through the current pH workflow.
- Dissolved oxygen sensing can provide important water-quality information.
- Turbidity sensing can provide an indication of suspended material.
- Water-level sensing can detect abnormal pond levels.
- Electrical monitoring can track equipment operating parameters.
- Additional sensors can be connected through ESP32 interfaces.
- Sensor readings can be timestamped.
- Historical readings can be stored in SQLite.
- Sensor values can be visualized in the Android application.

---

## POTENTIAL ACTUATORS

- Aerators can be controlled through relays.
- Water pumps can be controlled through additional relay channels.
- Feeding mechanisms can be automated.
- Filtration equipment can be controlled where appropriate.
- Lighting can be controlled as an optional subsystem.
- Actuator commands should use explicit states.
- Relay channels should be documented against their physical equipment.
- Electrical loads must be isolated from ESP32 GPIO circuitry.
- Appropriate protective devices should be used.
- Physical installation should follow applicable electrical safety requirements.

---

## ANDROID REPOSITORY LAYER

- The repository layer separates network operations from the UI.
- The aerator screen requests pond aerators through the repository.
- A successful result updates the aerator list.
- A failed result updates the server-error state.
- This separation makes UI code easier to maintain.
- The repository can later support caching.
- The repository can later support retry logic.
- The repository can later support authentication.
- The repository can later expose sensor history.
- The repository can later expose device health information.

---

## UI STATE MANAGEMENT

- The application maintains the currently selected pond.
- The application maintains the current navigation page.
- The application maintains the pond list.
- The aerator screen maintains an aerator list.
- The aerator screen maintains a loading state.
- The aerator screen maintains a server-error state.
- The aerator screen maintains a busy-aerator set.
- The pH screen maintains a pH value.
- The pH screen maintains an online state.
- The pH screen maintains a loading state.

---

## ERROR HANDLING

- Network requests can fail and must be handled without crashing the application.
- The aerator screen reports a Raspberry Pi connection error when the request fails.
- The displayed error can use the exception message when available.
- The UI exits the loading state after either success or failure.
- BLE connection failures should be communicated to the user.
- Sensor failures should be distinguishable from network failures.
- Camera failures should be reported separately.
- Backend failures should be logged for diagnosis.
- ESP32 HTTP failures should be visible in serial logs during development.
- Production systems should provide structured error reporting.

---

## INSTALLATION

- Install Android Studio for the Android application.
- Install the Android SDK required by the project.
- Open the Android project in Android Studio.
- Allow Gradle to synchronize dependencies.
- Connect a physical Android device or use a suitable emulator.
- Build the Android application before testing hardware features.
- Install Python on the Raspberry Pi.
- Install the backend Python dependencies.
- Install and configure the ESP32 development environment.
- Configure the local network before testing end-to-end communication.

---

## PYTHON BACKEND SETUP

- Create a Python virtual environment for the backend.
- Activate the virtual environment before installing packages.
- Install FastAPI.
- Install Uvicorn.
- Install the SQLite-related project dependencies if required.
- Install any camera-processing dependencies used by the deployment.
- Configure the database location.
- Configure the server host and port.
- Start the FastAPI service.
- Verify that the Raspberry Pi can accept local network requests.

---

## ESP32 SETUP

- Install Arduino IDE or PlatformIO.
- Install the ESP32 board support package.
- Connect the ESP32 to the development computer.
- Select the correct board.
- Select the correct serial port.
- Configure the firmware before flashing.
- Do not publish real Wi-Fi credentials.
- Verify relay wiring before powering connected equipment.
- Flash the firmware.
- Open the serial monitor and verify startup messages.

---

## GITHUB SETUP

- Create a repository for the automated shrimp farming project.
- Use a clear repository name such as automated-shrimp-farming.
- Add a descriptive repository summary.
- Add this README file to the repository.
- Add a project-specific .gitignore.
- Do not upload virtual environments.
- Do not upload generated camera recordings.
- Do not upload passwords or tokens.
- Do not upload build directories unnecessarily.
- Use meaningful commits when adding project components.

---

## REPOSITORY STRUCTURE

- A recommended top-level directory is android/.
- A recommended top-level directory is backend/.
- A recommended top-level directory is esp32/.
- A recommended top-level directory is raspberry-pi/.
- A recommended top-level directory is docs/.
- A recommended top-level directory is assets/.
- A requirements.txt file can describe Python dependencies.
- A .gitignore file should exclude generated and sensitive content.
- A README.md file should explain setup and architecture.
- A LICENSE file can be added when the project license is selected.

---

## RECOMMENDED API DESIGN

- GET /status can provide backend status.
- GET /ponds can provide pond information.
- GET /ponds/{pond_id} can provide a pond record.
- GET /ponds/{pond_id}/aerators can provide aerator information.
- POST /devices/register can register a controller.
- POST /relay can control a relay through the ESP32.
- GET /sensors can provide current sensor values.
- GET /sensors/history can provide historical readings.
- POST /feeding can trigger a feeding operation.
- GET /alerts can provide active system alerts.

---

## DEVICE REGISTRATION

- Device registration associates a field controller with the backend.
- The current ESP32 registration path is /api/devices/register.
- The device name identifies the controller.
- The IP address identifies the controller on the local network.
- The pond ID identifies the pond associated with the controller.
- The backend can reject invalid registration data.
- The backend can update an existing device record.
- The backend can store a last-seen timestamp.
- The backend can expose device online status.
- This registration model supports multiple ESP32 controllers.

---

## MULTI-POND SUPPORT

- The Android application can store multiple ponds.
- Each pond is selected independently.
- Pond-specific configuration is persisted.
- Aerators can be associated with a selected pond.
- pH configuration can be associated with a selected pond.
- The backend can store multiple pond records.
- Each pond can have different equipment.
- Future versions can support pond-specific thresholds.
- Future versions can support pond-specific feeding schedules.
- Future versions can support farm-wide dashboards.

---

## DATA MODEL

- A pond entity represents a farm pond.
- A device entity represents an ESP32 or other controller.
- An aerator entity represents an individual aeration unit.
- A sensor entity can represent a physical measurement device.
- A sensor reading represents a timestamped value.
- An alert entity can represent an abnormal condition.
- An equipment event can represent an actuator operation.
- A feeding event can represent a feeding operation.
- Device-to-pond relationships allow distributed hardware.
- Historical tables can support trend analysis.

---

## SENSOR HISTORY

- Sensor history can be recorded with timestamps.
- Temperature history can be plotted against time.
- pH history can be plotted against time.
- Dissolved oxygen history can be plotted against time.
- Water-level history can be plotted against time.
- Electrical energy history can be plotted against time.
- Historical records can support troubleshooting.
- Historical records can support operational analysis.
- Data retention should be configured for the deployment size.
- Database indexes should be added when history becomes large.

---

## ALERTING

- The platform can generate alerts when configured conditions are detected.
- Low water level can be treated as an alert condition.
- Sensor communication loss can be treated as an alert condition.
- Unexpected equipment state can be treated as an alert condition.
- A camera event can be treated as an alert condition.
- A backend connectivity failure can be displayed to the operator.
- Alerts should include a timestamp.
- Alerts should identify the affected pond or device.
- Alerts should provide an actionable description.
- Repeated alerts should be rate-limited when appropriate.

---

## CAMERA AND MOTION DETECTION

- A Raspberry Pi camera can provide continuous pond observation.
- Motion detection can compare consecutive frames.
- A motion event can be timestamped.
- The system can optionally save a short event clip.
- Large recordings should be stored outside Git.
- Night monitoring requires suitable camera and illumination hardware.
- The camera service should recover after temporary camera failures.
- The Android application can display camera availability.
- Future versions can use object detection.
- Future versions can analyze feeding activity.

---

## COMPUTER VISION ROADMAP

- Future computer vision can detect motion around the pond.
- Future computer vision can detect unusual activity.
- Future computer vision can monitor feeding response.
- Future computer vision can identify selected objects.
- Future computer vision can analyze time-based activity patterns.
- Future computer vision can combine image events with sensor readings.
- Machine-learning models should be evaluated before operational deployment.
- False positives should be measured.
- False negatives should be measured.
- Computer vision should be treated as an assistive monitoring layer until validated.

---

## AUTOMATED FEEDING

- A future feeding subsystem can be connected to the controller.
- The Android application can provide feeding schedules.
- The backend can store feeding events.
- The ESP32 can control a feeder actuator.
- Feeding commands should include a duration or defined dispense operation.
- Manual feeding can remain available as an override.
- The system should prevent accidental repeated activation.
- Schedules should be associated with a pond.
- Feeding records can be used for later analysis.
- Actual feed quantities and schedules should be determined from farm-management requirements.

---

## WATER MANAGEMENT

- A future pump controller can automate water circulation.
- Water-level sensing can trigger operator alerts.
- Pump operation can be logged.
- The application can display pump state.
- The backend can store pump events.
- Safety interlocks can prevent dry-running when appropriate.
- The controller should fail to a predefined safe state.
- Manual control should remain available when appropriate.
- Water-management thresholds should be configurable.
- Physical pump installation requires appropriate electrical protection.

---

## ELECTRICAL MONITORING

- Electrical parameters can provide information about equipment operation.
- Voltage can be displayed in volts.
- Frequency can be displayed in hertz.
- Power factor can be displayed for supported measurement hardware.
- Current can be displayed when measured.
- Power can be calculated or reported when supported.
- Energy can be accumulated in kilowatt-hours.
- A last-update time indicates freshness of the measurement.
- Unexpected electrical behavior can trigger an alert.
- Electrical measurements should use properly rated sensing hardware.

---

## PERFORMANCE

- Real-time IoT systems should minimize unnecessary network traffic.
- Sensor polling intervals should be selected according to the required response time.
- Camera processing should use an appropriate resolution.
- Database writes should not occur excessively often without a reason.
- The Android UI should remain responsive while network operations run.
- The backend should avoid blocking operations where asynchronous processing is appropriate.
- ESP32 firmware should avoid long blocking delays.
- Network reconnection should use reasonable retry intervals.
- Logs should be rotated in long-running deployments.
- Performance should be measured on the actual deployment hardware.

---

## RELIABILITY

- The system should continue operating safely when a network connection is interrupted.
- The ESP32 should restore Wi-Fi when possible.
- The Android application should show connection errors.
- The backend should track device availability.
- Sensor failures should be distinguishable from zero-valued measurements.
- Relay outputs should initialize to a known safe state.
- The ESP32 currently initializes active-low relays to OFF.
- Configuration should survive normal device restarts when stored persistently.
- The database should be backed up before major changes.
- Hardware should be tested under realistic environmental conditions.

---

## ELECTRICAL SAFETY

- ESP32 GPIO pins must not directly drive high-power equipment.
- Use appropriately rated relay modules or contactors.
- Keep mains wiring physically separated from low-voltage electronics.
- Use suitable fuses and circuit protection.
- Use waterproof enclosures in wet farm environments.
- Provide strain relief for cables.
- Protect connectors from moisture.
- Use proper grounding where required.
- Have qualified personnel handle hazardous mains wiring.
- Test emergency shutdown behavior before deployment.

---

## ENVIRONMENTAL PROTECTION

- Aquaculture installations expose electronics to moisture.
- Electronics should be installed in suitable enclosures.
- Cable glands should be used where appropriate.
- Condensation should be considered.
- Heat dissipation should be provided without compromising water protection.
- Sensors should be mounted according to their specifications.
- Sensor cables should be protected from mechanical damage.
- Equipment should be inspected periodically.
- Corrosion should be monitored.
- Maintenance procedures should be documented.

---

## CALIBRATION

- Sensors should be calibrated according to manufacturer procedures.
- pH sensors require appropriate calibration solutions.
- Temperature sensors should be checked against a reference.
- Electrical sensors should be checked against suitable instruments.
- Calibration constants should be stored securely.
- Calibration dates should be recorded.
- Sensor drift should be monitored.
- Failed calibration should generate a maintenance task.
- Calibration should be repeated at an appropriate interval.
- Do not assume an uncalibrated sensor is accurate.

---

## TESTING STRATEGY

- Test each hardware component independently.
- Test each software module independently.
- Test the Android application without hardware when possible.
- Test the backend using simulated requests.
- Test the ESP32 using the serial monitor.
- Test each relay channel individually.
- Test Wi-Fi loss and recovery.
- Test device registration.
- Test sensor failures.
- Test the complete end-to-end workflow.

---

## END TO END TEST

- Start the Raspberry Pi backend.
- Confirm that the database is available.
- Power the ESP32.
- Confirm that the ESP32 joins Wi-Fi.
- Confirm that the ESP32 registers with the backend.
- Open the Android application.
- Select the target pond.
- Open the aerator management screen.
- Verify the aerator state.
- Issue a control command and confirm the physical relay state.

---

## TROUBLESHOOTING

- If the Android application cannot retrieve aerators, verify the Raspberry Pi IP address.
- If the backend is unreachable, verify that the FastAPI process is running.
- If port 8000 is unavailable, check for another process using it.
- If the ESP32 does not register, verify Wi-Fi connectivity.
- If the ESP32 registers but control fails, verify the device IP address.
- If a relay behaves incorrectly, verify whether the relay module is active-low.
- If pH data is unavailable, verify the configured token and virtual pin.
- If camera monitoring fails, verify camera permissions and device access.
- If the Android UI reports an error, inspect the backend logs.
- If a sensor produces implausible values, recalibrate and inspect wiring.

---

## DEVELOPMENT WORKFLOW

- Define a feature before implementing it.
- Update the data model if new persistent information is required.
- Add or update backend endpoints.
- Update the repository layer.
- Update Android state and UI.
- Update ESP32 firmware if hardware control is required.
- Test each layer independently.
- Run an end-to-end test.
- Document the change.
- Commit the feature with a clear Git message.

---

## FUTURE MACHINE LEARNING

- Historical sensor data can be used for analytics.
- A model can learn normal operating patterns.
- Anomaly detection can identify unusual sensor combinations.
- Predictive models can estimate trends.
- Computer vision can provide additional visual features.
- Training data should be representative of the deployment environment.
- Models should be validated using separate test data.
- Model outputs should not directly control critical equipment without safeguards.
- Human review can remain part of the control loop.
- Model versions should be tracked.

---

## SCALABILITY

- The architecture can support more than one pond.
- Multiple ESP32 controllers can be registered.
- Each controller can be associated with a pond.
- Each pond can have its own equipment.
- The backend can centralize device records.
- The Android application can select individual ponds.
- Future versions can support farm-level aggregation.
- The database can be migrated to a larger database when necessary.
- The API can be placed behind a reverse proxy.
- Cloud deployment can be considered after local operation is stable.

---

## MAINTENANCE

- Inspect physical wiring regularly.
- Inspect relay modules regularly.
- Check sensor calibration.
- Check Wi-Fi connectivity.
- Check backend availability.
- Check database health.
- Review application logs.
- Review ESP32 serial logs during maintenance.
- Update software dependencies carefully.
- Back up configuration and data before major updates.

---

## PRIVACY

- Camera systems can capture people and surrounding areas.
- Camera access should be limited to required users.
- Avoid unnecessary recording.
- Store recordings only when required.
- Secure remote camera access.
- Do not expose camera streams publicly without appropriate protection.
- Document retention periods.
- Protect application credentials.
- Protect farm operational data.
- Review privacy requirements before deployment.

---

## PROJECT ROADMAP

- Complete robust pond management.
- Expand sensor integration.
- Improve aerator automation.
- Add historical sensor graphs.
- Add comprehensive device health monitoring.
- Add camera streaming.
- Add motion detection.
- Add automated feeding.
- Add advanced alerts.
- Add predictive analytics.
- Add multi-pond dashboards.
- Add cloud synchronization.
- Improve authentication.
- Improve deployment tooling.
- Create production-ready documentation.

---

## CONTRIBUTING

- Fork the repository.
- Create a feature branch.
- Make focused changes.
- Test the changes.
- Document the changes.
- Commit using a meaningful message.
- Push the branch.
- Open a pull request.
- Describe the problem and solution.
- Include testing information in the pull request.

---

## LICENSE AND USAGE

- Select an appropriate open-source license before distributing the project.
- Keep third-party license requirements in the repository.
- Do not redistribute credentials.
- Do not redistribute private camera recordings.
- Review library licenses before commercial deployment.
- Document externally hosted services.
- Document required hardware licenses where applicable.
- Document any proprietary sensor libraries.
- Keep attribution notices where required.
- Review the final repository before publishing.

---

## AUTHOR AND PROJECT

- Project name: ShrimpCaring.
- Project type: Automated shrimp farming and monitoring system.
- Primary application platform: Android.
- Primary mobile language: Kotlin.
- Backend technology: Python and FastAPI.
- Database technology: SQLite.
- Embedded controller: ESP32.
- Edge computer: Raspberry Pi.
- Primary communication technologies: Wi-Fi, HTTP, and BLE provisioning.
- Project focus: IoT, automation, monitoring, and smart aquaculture.

---

## APPENDIX 1: ANDROID IMPLEMENTATION NOTES

1. Confirm that the component has a clearly defined responsibility.

2. Confirm that configuration is separated from application logic.

3. Confirm that failures are handled explicitly.

4. Confirm that logs provide enough information for diagnosis.

5. Confirm that secrets are not stored in source code.

6. Confirm that network operations have appropriate timeouts.

7. Confirm that user-visible errors are understandable.

8. Confirm that state transitions are deterministic.

9. Confirm that the component has been tested independently.

10. Confirm that the component has been tested as part of the complete system.

11. Confirm that the README reflects the current implementation.

12. Confirm that generated files are excluded from Git.

13. Confirm that deployment instructions are reproducible.

14. Confirm that hardware changes are documented.

15. Confirm that software dependencies are documented.

16. Confirm that the component has a safe failure mode.

17. Confirm that maintenance requirements are documented.

18. Confirm that future extension points are identified.

19. Confirm that performance is acceptable on target hardware.

20. Confirm that the final implementation is ready for the next integration stage.

---

## APPENDIX 2: BACKEND IMPLEMENTATION NOTES

1. Confirm that the component has a clearly defined responsibility.

2. Confirm that configuration is separated from application logic.

3. Confirm that failures are handled explicitly.

4. Confirm that logs provide enough information for diagnosis.

5. Confirm that secrets are not stored in source code.

6. Confirm that network operations have appropriate timeouts.

7. Confirm that user-visible errors are understandable.

8. Confirm that state transitions are deterministic.

9. Confirm that the component has been tested independently.

10. Confirm that the component has been tested as part of the complete system.

11. Confirm that the README reflects the current implementation.

12. Confirm that generated files are excluded from Git.

13. Confirm that deployment instructions are reproducible.

14. Confirm that hardware changes are documented.

15. Confirm that software dependencies are documented.

16. Confirm that the component has a safe failure mode.

17. Confirm that maintenance requirements are documented.

18. Confirm that future extension points are identified.

19. Confirm that performance is acceptable on target hardware.

20. Confirm that the final implementation is ready for the next integration stage.

---

## APPENDIX 3: ESP32 IMPLEMENTATION NOTES

1. Confirm that the component has a clearly defined responsibility.

2. Confirm that configuration is separated from application logic.

3. Confirm that failures are handled explicitly.

4. Confirm that logs provide enough information for diagnosis.

5. Confirm that secrets are not stored in source code.

6. Confirm that network operations have appropriate timeouts.

7. Confirm that user-visible errors are understandable.

8. Confirm that state transitions are deterministic.

9. Confirm that the component has been tested independently.

10. Confirm that the component has been tested as part of the complete system.

11. Confirm that the README reflects the current implementation.

12. Confirm that generated files are excluded from Git.

13. Confirm that deployment instructions are reproducible.

14. Confirm that hardware changes are documented.

15. Confirm that software dependencies are documented.

16. Confirm that the component has a safe failure mode.

17. Confirm that maintenance requirements are documented.

18. Confirm that future extension points are identified.

19. Confirm that performance is acceptable on target hardware.

20. Confirm that the final implementation is ready for the next integration stage.

---

## APPENDIX 4: BLE IMPLEMENTATION NOTES

1. Confirm that the component has a clearly defined responsibility.

2. Confirm that configuration is separated from application logic.

3. Confirm that failures are handled explicitly.

4. Confirm that logs provide enough information for diagnosis.

5. Confirm that secrets are not stored in source code.

6. Confirm that network operations have appropriate timeouts.

7. Confirm that user-visible errors are understandable.

8. Confirm that state transitions are deterministic.

9. Confirm that the component has been tested independently.

10. Confirm that the component has been tested as part of the complete system.

11. Confirm that the README reflects the current implementation.

12. Confirm that generated files are excluded from Git.

13. Confirm that deployment instructions are reproducible.

14. Confirm that hardware changes are documented.

15. Confirm that software dependencies are documented.

16. Confirm that the component has a safe failure mode.

17. Confirm that maintenance requirements are documented.

18. Confirm that future extension points are identified.

19. Confirm that performance is acceptable on target hardware.

20. Confirm that the final implementation is ready for the next integration stage.

---

## APPENDIX 5: DATABASE IMPLEMENTATION NOTES

1. Confirm that the component has a clearly defined responsibility.

2. Confirm that configuration is separated from application logic.

3. Confirm that failures are handled explicitly.

4. Confirm that logs provide enough information for diagnosis.

5. Confirm that secrets are not stored in source code.

6. Confirm that network operations have appropriate timeouts.

7. Confirm that user-visible errors are understandable.

8. Confirm that state transitions are deterministic.

9. Confirm that the component has been tested independently.

10. Confirm that the component has been tested as part of the complete system.

11. Confirm that the README reflects the current implementation.

12. Confirm that generated files are excluded from Git.

13. Confirm that deployment instructions are reproducible.

14. Confirm that hardware changes are documented.

15. Confirm that software dependencies are documented.

16. Confirm that the component has a safe failure mode.

17. Confirm that maintenance requirements are documented.

18. Confirm that future extension points are identified.

19. Confirm that performance is acceptable on target hardware.

20. Confirm that the final implementation is ready for the next integration stage.

---

## APPENDIX 6: AERATOR IMPLEMENTATION NOTES

1. Confirm that the component has a clearly defined responsibility.

2. Confirm that configuration is separated from application logic.

3. Confirm that failures are handled explicitly.

4. Confirm that logs provide enough information for diagnosis.

5. Confirm that secrets are not stored in source code.

6. Confirm that network operations have appropriate timeouts.

7. Confirm that user-visible errors are understandable.

8. Confirm that state transitions are deterministic.

9. Confirm that the component has been tested independently.

10. Confirm that the component has been tested as part of the complete system.

11. Confirm that the README reflects the current implementation.

12. Confirm that generated files are excluded from Git.

13. Confirm that deployment instructions are reproducible.

14. Confirm that hardware changes are documented.

15. Confirm that software dependencies are documented.

16. Confirm that the component has a safe failure mode.

17. Confirm that maintenance requirements are documented.

18. Confirm that future extension points are identified.

19. Confirm that performance is acceptable on target hardware.

20. Confirm that the final implementation is ready for the next integration stage.

---

## APPENDIX 7: PH IMPLEMENTATION NOTES

1. Confirm that the component has a clearly defined responsibility.

2. Confirm that configuration is separated from application logic.

3. Confirm that failures are handled explicitly.

4. Confirm that logs provide enough information for diagnosis.

5. Confirm that secrets are not stored in source code.

6. Confirm that network operations have appropriate timeouts.

7. Confirm that user-visible errors are understandable.

8. Confirm that state transitions are deterministic.

9. Confirm that the component has been tested independently.

10. Confirm that the component has been tested as part of the complete system.

11. Confirm that the README reflects the current implementation.

12. Confirm that generated files are excluded from Git.

13. Confirm that deployment instructions are reproducible.

14. Confirm that hardware changes are documented.

15. Confirm that software dependencies are documented.

16. Confirm that the component has a safe failure mode.

17. Confirm that maintenance requirements are documented.

18. Confirm that future extension points are identified.

19. Confirm that performance is acceptable on target hardware.

20. Confirm that the final implementation is ready for the next integration stage.

---

## APPENDIX 8: CAMERA IMPLEMENTATION NOTES

1. Confirm that the component has a clearly defined responsibility.

2. Confirm that configuration is separated from application logic.

3. Confirm that failures are handled explicitly.

4. Confirm that logs provide enough information for diagnosis.

5. Confirm that secrets are not stored in source code.

6. Confirm that network operations have appropriate timeouts.

7. Confirm that user-visible errors are understandable.

8. Confirm that state transitions are deterministic.

9. Confirm that the component has been tested independently.

10. Confirm that the component has been tested as part of the complete system.

11. Confirm that the README reflects the current implementation.

12. Confirm that generated files are excluded from Git.

13. Confirm that deployment instructions are reproducible.

14. Confirm that hardware changes are documented.

15. Confirm that software dependencies are documented.

16. Confirm that the component has a safe failure mode.

17. Confirm that maintenance requirements are documented.

18. Confirm that future extension points are identified.

19. Confirm that performance is acceptable on target hardware.

20. Confirm that the final implementation is ready for the next integration stage.

---

## APPENDIX 9: NETWORK IMPLEMENTATION NOTES

1. Confirm that the component has a clearly defined responsibility.

2. Confirm that configuration is separated from application logic.

3. Confirm that failures are handled explicitly.

4. Confirm that logs provide enough information for diagnosis.

5. Confirm that secrets are not stored in source code.

6. Confirm that network operations have appropriate timeouts.

7. Confirm that user-visible errors are understandable.

8. Confirm that state transitions are deterministic.

9. Confirm that the component has been tested independently.

10. Confirm that the component has been tested as part of the complete system.

11. Confirm that the README reflects the current implementation.

12. Confirm that generated files are excluded from Git.

13. Confirm that deployment instructions are reproducible.

14. Confirm that hardware changes are documented.

15. Confirm that software dependencies are documented.

16. Confirm that the component has a safe failure mode.

17. Confirm that maintenance requirements are documented.

18. Confirm that future extension points are identified.

19. Confirm that performance is acceptable on target hardware.

20. Confirm that the final implementation is ready for the next integration stage.

---

## APPENDIX 10: SECURITY IMPLEMENTATION NOTES

1. Confirm that the component has a clearly defined responsibility.

2. Confirm that configuration is separated from application logic.

3. Confirm that failures are handled explicitly.

4. Confirm that logs provide enough information for diagnosis.

5. Confirm that secrets are not stored in source code.

6. Confirm that network operations have appropriate timeouts.

7. Confirm that user-visible errors are understandable.

8. Confirm that state transitions are deterministic.

9. Confirm that the component has been tested independently.

10. Confirm that the component has been tested as part of the complete system.

11. Confirm that the README reflects the current implementation.

12. Confirm that generated files are excluded from Git.

13. Confirm that deployment instructions are reproducible.

14. Confirm that hardware changes are documented.

15. Confirm that software dependencies are documented.

16. Confirm that the component has a safe failure mode.

17. Confirm that maintenance requirements are documented.

18. Confirm that future extension points are identified.

19. Confirm that performance is acceptable on target hardware.

20. Confirm that the final implementation is ready for the next integration stage.

---

## APPENDIX 11: DEPLOYMENT CHECKLIST

1. Confirm that the component has a clearly defined responsibility.

2. Confirm that configuration is separated from application logic.

3. Confirm that failures are handled explicitly.

4. Confirm that logs provide enough information for diagnosis.

5. Confirm that secrets are not stored in source code.

6. Confirm that network operations have appropriate timeouts.

7. Confirm that user-visible errors are understandable.

8. Confirm that state transitions are deterministic.

9. Confirm that the component has been tested independently.

10. Confirm that the component has been tested as part of the complete system.

11. Confirm that the README reflects the current implementation.

12. Confirm that generated files are excluded from Git.

13. Confirm that deployment instructions are reproducible.

14. Confirm that hardware changes are documented.

15. Confirm that software dependencies are documented.

16. Confirm that the component has a safe failure mode.

17. Confirm that maintenance requirements are documented.

18. Confirm that future extension points are identified.

19. Confirm that performance is acceptable on target hardware.

20. Confirm that the final implementation is ready for the next integration stage.

---

## APPENDIX 12: HARDWARE CHECKLIST

1. Confirm that the component has a clearly defined responsibility.

2. Confirm that configuration is separated from application logic.

3. Confirm that failures are handled explicitly.

4. Confirm that logs provide enough information for diagnosis.

5. Confirm that secrets are not stored in source code.

6. Confirm that network operations have appropriate timeouts.

7. Confirm that user-visible errors are understandable.

8. Confirm that state transitions are deterministic.

9. Confirm that the component has been tested independently.

10. Confirm that the component has been tested as part of the complete system.

11. Confirm that the README reflects the current implementation.

12. Confirm that generated files are excluded from Git.

13. Confirm that deployment instructions are reproducible.

14. Confirm that hardware changes are documented.

15. Confirm that software dependencies are documented.

16. Confirm that the component has a safe failure mode.

17. Confirm that maintenance requirements are documented.

18. Confirm that future extension points are identified.

19. Confirm that performance is acceptable on target hardware.

20. Confirm that the final implementation is ready for the next integration stage.

---

## APPENDIX 13: SOFTWARE CHECKLIST

1. Confirm that the component has a clearly defined responsibility.

2. Confirm that configuration is separated from application logic.

3. Confirm that failures are handled explicitly.

4. Confirm that logs provide enough information for diagnosis.

5. Confirm that secrets are not stored in source code.

6. Confirm that network operations have appropriate timeouts.

7. Confirm that user-visible errors are understandable.

8. Confirm that state transitions are deterministic.

9. Confirm that the component has been tested independently.

10. Confirm that the component has been tested as part of the complete system.

11. Confirm that the README reflects the current implementation.

12. Confirm that generated files are excluded from Git.

13. Confirm that deployment instructions are reproducible.

14. Confirm that hardware changes are documented.

15. Confirm that software dependencies are documented.

16. Confirm that the component has a safe failure mode.

17. Confirm that maintenance requirements are documented.

18. Confirm that future extension points are identified.

19. Confirm that performance is acceptable on target hardware.

20. Confirm that the final implementation is ready for the next integration stage.

---

## APPENDIX 14: FIELD TEST CHECKLIST

1. Confirm that the component has a clearly defined responsibility.

2. Confirm that configuration is separated from application logic.

3. Confirm that failures are handled explicitly.

4. Confirm that logs provide enough information for diagnosis.

5. Confirm that secrets are not stored in source code.

6. Confirm that network operations have appropriate timeouts.

7. Confirm that user-visible errors are understandable.

8. Confirm that state transitions are deterministic.

9. Confirm that the component has been tested independently.

10. Confirm that the component has been tested as part of the complete system.

11. Confirm that the README reflects the current implementation.

12. Confirm that generated files are excluded from Git.

13. Confirm that deployment instructions are reproducible.

14. Confirm that hardware changes are documented.

15. Confirm that software dependencies are documented.

16. Confirm that the component has a safe failure mode.

17. Confirm that maintenance requirements are documented.

18. Confirm that future extension points are identified.

19. Confirm that performance is acceptable on target hardware.

20. Confirm that the final implementation is ready for the next integration stage.

---

## APPENDIX 15: GITHUB CHECKLIST

1. Confirm that the component has a clearly defined responsibility.

2. Confirm that configuration is separated from application logic.

3. Confirm that failures are handled explicitly.

4. Confirm that logs provide enough information for diagnosis.

5. Confirm that secrets are not stored in source code.

6. Confirm that network operations have appropriate timeouts.

7. Confirm that user-visible errors are understandable.

8. Confirm that state transitions are deterministic.

9. Confirm that the component has been tested independently.

10. Confirm that the component has been tested as part of the complete system.

11. Confirm that the README reflects the current implementation.

12. Confirm that generated files are excluded from Git.

13. Confirm that deployment instructions are reproducible.

14. Confirm that hardware changes are documented.

15. Confirm that software dependencies are documented.

16. Confirm that the component has a safe failure mode.

17. Confirm that maintenance requirements are documented.

18. Confirm that future extension points are identified.

19. Confirm that performance is acceptable on target hardware.

20. Confirm that the final implementation is ready for the next integration stage.

---

## API REFERENCE

- Document the HTTP method. Cycle 1.

- Document the endpoint path. Cycle 1.

- Document required parameters. Cycle 1.

- Document the expected request body. Cycle 1.

- Document the expected response. Cycle 1.

- Document possible error responses. Cycle 1.

- Document authentication requirements. Cycle 1.

- Document the device or application that calls the endpoint. Cycle 1.

- Document whether the operation changes persistent state. Cycle 1.

- Document whether the operation controls physical equipment. Cycle 1.

- Document the HTTP method. Cycle 2.

- Document the endpoint path. Cycle 2.

- Document required parameters. Cycle 2.

- Document the expected request body. Cycle 2.

- Document the expected response. Cycle 2.

- Document possible error responses. Cycle 2.

- Document authentication requirements. Cycle 2.

- Document the device or application that calls the endpoint. Cycle 2.

- Document whether the operation changes persistent state. Cycle 2.

- Document whether the operation controls physical equipment. Cycle 2.

- Document the HTTP method. Cycle 3.

- Document the endpoint path. Cycle 3.

- Document required parameters. Cycle 3.

- Document the expected request body. Cycle 3.

- Document the expected response. Cycle 3.

- Document possible error responses. Cycle 3.

- Document authentication requirements. Cycle 3.

- Document the device or application that calls the endpoint. Cycle 3.

- Document whether the operation changes persistent state. Cycle 3.

- Document whether the operation controls physical equipment. Cycle 3.

- Document the HTTP method. Cycle 4.

- Document the endpoint path. Cycle 4.

- Document required parameters. Cycle 4.

- Document the expected request body. Cycle 4.

- Document the expected response. Cycle 4.

- Document possible error responses. Cycle 4.

- Document authentication requirements. Cycle 4.

- Document the device or application that calls the endpoint. Cycle 4.

- Document whether the operation changes persistent state. Cycle 4.

- Document whether the operation controls physical equipment. Cycle 4.

- Document the HTTP method. Cycle 5.

- Document the endpoint path. Cycle 5.

- Document required parameters. Cycle 5.

- Document the expected request body. Cycle 5.

- Document the expected response. Cycle 5.

- Document possible error responses. Cycle 5.

- Document authentication requirements. Cycle 5.

- Document the device or application that calls the endpoint. Cycle 5.

- Document whether the operation changes persistent state. Cycle 5.

- Document whether the operation controls physical equipment. Cycle 5.

- Document the HTTP method. Cycle 6.

- Document the endpoint path. Cycle 6.

- Document required parameters. Cycle 6.

- Document the expected request body. Cycle 6.

- Document the expected response. Cycle 6.

- Document possible error responses. Cycle 6.

- Document authentication requirements. Cycle 6.

- Document the device or application that calls the endpoint. Cycle 6.

- Document whether the operation changes persistent state. Cycle 6.

- Document whether the operation controls physical equipment. Cycle 6.

- Document the HTTP method. Cycle 7.

- Document the endpoint path. Cycle 7.

- Document required parameters. Cycle 7.

- Document the expected request body. Cycle 7.

- Document the expected response. Cycle 7.

- Document possible error responses. Cycle 7.

- Document authentication requirements. Cycle 7.

- Document the device or application that calls the endpoint. Cycle 7.

- Document whether the operation changes persistent state. Cycle 7.

- Document whether the operation controls physical equipment. Cycle 7.

- Document the HTTP method. Cycle 8.

- Document the endpoint path. Cycle 8.

- Document required parameters. Cycle 8.

- Document the expected request body. Cycle 8.

- Document the expected response. Cycle 8.

- Document possible error responses. Cycle 8.

- Document authentication requirements. Cycle 8.

- Document the device or application that calls the endpoint. Cycle 8.

- Document whether the operation changes persistent state. Cycle 8.

- Document whether the operation controls physical equipment. Cycle 8.

---

## FIELD DEPLOYMENT REFERENCE

- Verify enclosure protection. Cycle 1.

- Verify cable routing. Cycle 1.

- Verify power supplies. Cycle 1.

- Verify relay isolation. Cycle 1.

- Verify sensor mounting. Cycle 1.

- Verify network coverage. Cycle 1.

- Verify Raspberry Pi placement. Cycle 1.

- Verify ESP32 placement. Cycle 1.

- Verify camera placement. Cycle 1.

- Verify maintenance access. Cycle 1.

- Verify enclosure protection. Cycle 2.

- Verify cable routing. Cycle 2.

- Verify power supplies. Cycle 2.

- Verify relay isolation. Cycle 2.

- Verify sensor mounting. Cycle 2.

- Verify network coverage. Cycle 2.

- Verify Raspberry Pi placement. Cycle 2.

- Verify ESP32 placement. Cycle 2.

- Verify camera placement. Cycle 2.

- Verify maintenance access. Cycle 2.

- Verify enclosure protection. Cycle 3.

- Verify cable routing. Cycle 3.

- Verify power supplies. Cycle 3.

- Verify relay isolation. Cycle 3.

- Verify sensor mounting. Cycle 3.

- Verify network coverage. Cycle 3.

- Verify Raspberry Pi placement. Cycle 3.

- Verify ESP32 placement. Cycle 3.

- Verify camera placement. Cycle 3.

- Verify maintenance access. Cycle 3.

- Verify enclosure protection. Cycle 4.

- Verify cable routing. Cycle 4.

- Verify power supplies. Cycle 4.

- Verify relay isolation. Cycle 4.

- Verify sensor mounting. Cycle 4.

- Verify network coverage. Cycle 4.

- Verify Raspberry Pi placement. Cycle 4.

- Verify ESP32 placement. Cycle 4.

- Verify camera placement. Cycle 4.

- Verify maintenance access. Cycle 4.

- Verify enclosure protection. Cycle 5.

- Verify cable routing. Cycle 5.

- Verify power supplies. Cycle 5.

- Verify relay isolation. Cycle 5.

- Verify sensor mounting. Cycle 5.

- Verify network coverage. Cycle 5.

- Verify Raspberry Pi placement. Cycle 5.

- Verify ESP32 placement. Cycle 5.

- Verify camera placement. Cycle 5.

- Verify maintenance access. Cycle 5.

- Verify enclosure protection. Cycle 6.

- Verify cable routing. Cycle 6.

- Verify power supplies. Cycle 6.

- Verify relay isolation. Cycle 6.

- Verify sensor mounting. Cycle 6.

- Verify network coverage. Cycle 6.

- Verify Raspberry Pi placement. Cycle 6.

- Verify ESP32 placement. Cycle 6.

- Verify camera placement. Cycle 6.

- Verify maintenance access. Cycle 6.

- Verify enclosure protection. Cycle 7.

- Verify cable routing. Cycle 7.

- Verify power supplies. Cycle 7.

- Verify relay isolation. Cycle 7.

- Verify sensor mounting. Cycle 7.

- Verify network coverage. Cycle 7.

- Verify Raspberry Pi placement. Cycle 7.

- Verify ESP32 placement. Cycle 7.

- Verify camera placement. Cycle 7.

- Verify maintenance access. Cycle 7.

- Verify enclosure protection. Cycle 8.

- Verify cable routing. Cycle 8.

- Verify power supplies. Cycle 8.

- Verify relay isolation. Cycle 8.

- Verify sensor mounting. Cycle 8.

- Verify network coverage. Cycle 8.

- Verify Raspberry Pi placement. Cycle 8.

- Verify ESP32 placement. Cycle 8.

- Verify camera placement. Cycle 8.

- Verify maintenance access. Cycle 8.

