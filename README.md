# uifx

uifx is a customizable RuneLite plugin that replaces default UI icons (such as sidebar tabs) with user-defined static or animated images.

## Features

- Supports both static icons and frame-based animations
- Load custom images from a local folder
- Per-tab toggles and animation speed configuration
- Clean overlay integration with RuneLite
- Button to open the local resource folder

## Folder Structure

Place your custom icons in the following directory:
~/.runelite/uifx/


Each sidebar tab can use:

- A single image for a static icon:
    - inventory.png


- Or multiple images for an animated icon:

  - inventory_0.png
  - inventory_1.png
  - inventory_2.png


You can adjust frame delay and toggle icons individually through the plugin settings panel.

## Configuration

uifx includes a settings menu within RuneLite where you can:

- Enable or disable each icon individually
- Set the animation speed for each icon
- Open the image resource folder for quick access

## Installation

This plugin is not currently available through the Plugin Hub.

To build and test locally:

1. Clone this repository:


bash
git clone https://github.com/Lookout69/uifx.git