   
 inPosition inColour	 inTexture  vyColour	 vyTexture  uProjectionView� void main() {
   vyColour    = inColour;
   vyTexture   = inTexture;
   gl_Position = uProjectionView * vec4(inPosition, 0.0, 1.0);
}�  	 vyTexture vyColour	 uTexture0  outColorJ void main() {
    outColor = texture2D(uTexture0, vyTexture) * vyColour;
}�