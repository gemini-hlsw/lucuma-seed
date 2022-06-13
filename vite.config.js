const { defineConfig } = require('vite');
const react = require('@vitejs/plugin-react');
const { visualizer } = require('rollup-plugin-visualizer');
const path = require('path');
const fs = require('fs');
const ViteFonts = require('vite-plugin-fonts');
const mkcert = require('vite-plugin-mkcert');

const fontImport = ViteFonts.Plugin({
  google: {
    families: [
      {
        name: 'Lato',
        styles: 'ital,wght@0,400;0,700;1,400;1,700',
      },
    ],
  },
});

// https://vitejs.dev/config/
module.exports = ({ command, mode }) => {
  const scalaClassesDir = path.resolve(__dirname, 'web/target/scala-2.13');
  const isProduction = mode == 'production';
  const sjs = isProduction
    ? path.resolve(scalaClassesDir, 'web-opt')
    : path.resolve(scalaClassesDir, 'web-fastopt');
  const rollupPlugins = isProduction ? [] : [visualizer()];
  const common = path.resolve(__dirname, 'common/');
  const webappCommon = path.resolve(common, 'src/main/webapp/');
  const imagesCommon = path.resolve(webappCommon, 'images');
  const publicDirProd = path.resolve(common, 'src/main/public');
  const publicDirDev = path.resolve(common, 'src/main/publicdev');

  const publicDir = mode == 'production' ? publicDirProd : publicDirDev;
  return {
    // TODO Remove this if we get EnvironmentPlugin to work.
    root: 'web/src/main/webapp',
    publicDir: publicDir,
    envPrefix: ['VITE_', 'CATS_EFFECT_'],
    resolve: {
      dedupe: ['react-is'],
      alias: [
        {
          find: 'process',
          replacement: 'process/browser',
        },
        {
          find: '@sjs',
          replacement: sjs,
        },
        {
          find: '/common',
          replacement: webappCommon,
        },
        {
          find: '/images',
          replacement: imagesCommon,
        }
      ],
    },
    css: {
      preprocessorOptions: {
        scss: {
          charset: false,
        },
      },
    },
    server: {
      strictPort: true,
      fsServe: {
        strict: true,
      },
      host: '0.0.0.0',
      port: 8080,
      https: true,
      watch: {
        ignored: [
          function ignoreThisPath(_path) {
            const sjsIgnored =
              _path.includes('/target/stream') ||
              _path.includes('/zinc/') ||
              _path.includes('/classes') ||
              _path.endsWith('.tmp');
            return sjsIgnored;
          }
        ]
      }
    },
    build: {
      emptyOutDir: true,
      chunkSizeWarningLimit: 20000,
      terserOptions: {
        sourceMap: false,
        compress: {
          passes: 2,
          toplevel: true,
          ecma: 2015,
        },
      },
      rollupOptions: {
        plugins: rollupPlugins,
      },
      outDir: path.resolve(__dirname, 'deploy/static'),
    },
    plugins: [
      isProduction
        ? null
        : mkcert.default({ hosts: ['localhost', 'local.lucuma.xyz'] }),
      react(),
      fontImport
    ],
  };
};
