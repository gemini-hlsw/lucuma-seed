const { defineConfig } = require('vite');
const react = require('@vitejs/plugin-react');
// const reactPreset = require('vite-preset-react')
const path = require('path');
const fs = require('fs');
const mkcert = require('vite-plugin-mkcert');
// const externals = require('vite-plugin-externals')
// const resolveExternals = require('vite-plugin-resolve-externals')

// https://vitejs.dev/config/
module.exports = ({ command, mode }) => {
  const scalaClassesDir = path.resolve(__dirname, 'web/target/scala-3.1.3');
  const isProduction = mode == 'production';
  const sjs = isProduction
    ? path.resolve(scalaClassesDir, 'web-opt')
    : path.resolve(scalaClassesDir, 'web-fastopt');
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
      outDir: path.resolve(__dirname, 'deploy/static'),
    },
    plugins: [
      isProduction
        ? null
        : mkcert.default({ hosts: ['localhost', 'local.lucuma.xyz'] }),
      react()
      // reactPreset.default({injectReact: true})
      // resolveExternals({
      //   react: 'React'
      // })
      // externals.viteExternalsPlugin({
      //   react: 'React'
      // })
    ],
  };
};
