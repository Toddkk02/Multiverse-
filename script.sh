mkdir -p backup/src/main/resources
mkdir -p backup/src/client/resources
cp -r src/main/resources/* backup/src/main/resources/
cp -r src/client/resources/* backup/src/client/resources/

# Rimuoviamo i file mixin esistenti per evitare conflitti
rm -f src/main/resources/multiverse.mixins.json
rm -f src/client/resources/multiverse.client.mixins.json

# Creiamo il file mixin principale
cat > src/main/resources/multiverse.mixins.json << 'EOF'
{
  "required": true,
  "minVersion": "0.8",
  "package": "com.todd.multiverse.mixin",
  "compatibilityLevel": "JAVA_17",
  "mixins": [
    "ExampleMixin"
  ],
  "injectors": {
    "defaultRequire": 1
  }
}
EOF

# Creiamo il file mixin client
cat > src/client/resources/multiverse.client.mixins.json << 'EOF'
{
  "required": true,
  "minVersion": "0.8",
  "package": "com.todd.multiverse.mixin.client",
  "compatibilityLevel": "JAVA_17",
  "client": [
    "ExampleClientMixin"
  ],
  "injectors": {
    "defaultRequire": 1
  }
}
EOF

# Aggiorniamo fabric.mod.json
cp src/main/resources/fabric.mod.json src/main/resources/fabric.mod.json.bak
cat > src/main/resources/fabric.mod.json << 'EOF'
{
  "schemaVersion": 1,
  "id": "multiverse",
  "version": "1.0.0",
  "name": "Multiverse",
  "description": "A mod that adds new dimensions and items",
  "authors": [
    "Todd"
  ],
  "contact": {
    "homepage": "https://fabricmc.net/",
    "sources": "https://github.com/FabricMC/fabric-example-mod"
  },
  "license": "MIT",
  "icon": "assets/multiverse/icon.png",
  "environment": "*",
  "entrypoints": {
    "main": [
      "com.todd.multiverse.Multiverse"
    ],
    "client": [
      "com.todd.multiverse.MultiverseClient"
    ]
  },
  "mixins": [
    "multiverse.mixins.json",
    "multiverse.client.mixins.json"
  ],
  "depends": {
    "fabricloader": ">=0.14.21",
    "minecraft": "~1.19.2",
    "java": ">=17",
    "fabric-api": "*"
  },
  "suggests": {
    "another-mod": "*"
  }
}
EOF
