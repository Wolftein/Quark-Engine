    
inPosition inColour 	inTexture  vyColour 	vyTexture  uProjectionView �void main() {
   vyColour    = inColour;
   vyTexture   = inTexture;
   gl_Position = uProjectionView * vec4(inPosition, 0.0, 1.0);
}�   vyColour 	vyTexture  outColor 	uTexture0 Ivoid main() {
  outColor = texture2D(uTexture0, vyTexture) * vyColour;
 }�