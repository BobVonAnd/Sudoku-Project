#version 330 core
layout(location = 0) in vec3 aPos;
layout(location = 1) in vec2 aTexCoord;
out vec2 TexCoord;
uniform vec2 offset;
uniform vec2 scale;
uniform vec2 screenSize; 

void main() {
   vec3 pos = aPos;
   vec2 ndc = offset / screenSize * 2.0 - 1.0;
   pos.xy = pos.xy * scale / screenSize * 2.0 + ndc;
   gl_Position = vec4(pos,1.0);
   TexCoord = aTexCoord;
}

#version 330 core\n" +
in vec2 TexCoord;
out vec4 FragColor;
uniform sampler2D fontTexture;
uniform vec4 textColor; 
void main() {
    float alpha = texture(fontTexture, TexCoord).r;
    FragColor = vec4(textColor.rgb, textColor.a * alpha);
}

