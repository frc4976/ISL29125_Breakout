<div align="center"><img src="http://i.imgur.com/kMnwB2H.png"/></div>
<hr>

<h1>Integration</h1>
<ol type="1">
    <li><b><a href="https://www.sparkfun.com/products/12829">Purchase</a></b> and <b><a href="https://learn.sparkfun.com/tutorials/isl29125-rgb-light-sensor-hookup-guide">Hookup</a></b> the sensor to the NI roboRIO on your FIRST robot. <b>TIPS:</b>
        <ul>
            <li>Find or purchase a <b><a href="http://i.imgur.com/vP1dlOZ.jpg">4-pin female jumper</a>. Keep one end in tact and solder the other to the sensor</li>
            <li><b>IMPORTANT:</b> The SDA and SCL connections on the <b><a href="http://i.imgur.com/vjzoYtm.png">sensor</a></b> and on the <b><a href="http://i.imgur.com/DSNJLGU.png">NI roboRIO</a></b> are swapped. Watch out for this while soldering.</li>
        </ul>
    </li>
    <li>Install the <b><a href="https://github.com/frc4976/ISL29125_Breakout/blob/master/src/ca/_4976/color/ISL29125.java">ISL29125 library</a></b> on the NI roboRIO. <b>TIPS:</b>
        <ul>
            <li>Copy / Paste the file into your robot project directory</li>
        </ul>
    </li>
    <li>Integrate the library into your project. <b>TIPS:</b>
        <ul>
            <li>Initialize the sensor:
                <pre><code class="java">
                    //(in header)
                    //Create the sensor object
                    public ISL29125 colorSensor = new ISL29125();

                    //(in robotInit())
                    //Run the sensor initialization function (optional: print out the results)
                    System.out.println(colorSensor.init());
                </code></pre>
            </li>
            <li>Configure the sensor <i>(optional)</i>:
                <pre><code>
                    //(in robotInit())
                    //Configure the sensor (optional: print out the results)
                    System.out.println(colorSensor.config(CFG1, CFG2, CFG3));
                </code></pre>
                The full list of configuration variables are at the bottom of the library file
            </li>
            <li>Get color readings from the sensor:
                <pre><code>
                    //(in teleopPeriodic())
                    //Request a color reading and store the results
                    int[] colors = colorSensor.readColor();
                </code></pre>
            </li>
        </ul>
    </li>
    <li>Integrate the Dash.jar debug window <i>(optional)</i>. <b>TIPS:</b>
        <ul>
            <li>Dash.jar uses 3 NetworkTable values (ISL29125-0, ISL29125-1, ISL29125-2), and a table named "data":
                <pre><code>
                    //(in header)
                    //Create the NetworkTable object
                    public NetworkTable table = NetworkTable.getTable("data");

                    //(in robotInit())
                    //Add the default values
                    table.putNumber("ISL29125-0", 0); //The red value
                    table.putNumber("ISL29125-1", 0); //The green value
                    table.putNumber("ISL29125-2", 0); //The blue value
                </code></pre>
            </li>
            <li>These values are expected to take on a value from 0 - 255, while the ISL29125 sensor provides a 16-bit integer from 0 - 65535:
                <pre><code>
                    //(in teleopPeriodic())
                    //Add properly formatted color values (divide by 65535, multiply by 255)
                    for (int i = 0; i < colors.length; i++)
                        table.putNumber("ISL29125-" + i, (colors[i] / 65535.0) * 255);
                </code></pre>
            </li>
        </ul>
    </li>
</ol>