var rt = Object.defineProperty;
var st = (c, r, e) =>
  r in c
    ? rt(c, r, { enumerable: !0, configurable: !0, writable: !0, value: e })
    : (c[r] = e);
var s = (c, r, e) => st(c, typeof r != "symbol" ? r + "" : r, e);
import {
  C as P,
  V as $,
  M as He,
  a as Ye,
  D as lt,
  b as ct,
  G as mt,
  c as p,
  E as j,
  d as Ae,
  e as _,
  f as De,
  S as ge,
  g as Ge,
  T as dt,
  B as ee,
  h as Be,
  i as Z,
  L as Te,
  R as Xe,
  j as ue,
  k as se,
  p as S,
  l as K,
  m as ft,
  I as pe,
  P as ut,
  n as tt,
  o as Le,
  q as Ee,
  r as je,
  s as ht,
  t as G,
  u as vt,
  Q as pt,
  v as gt,
  w as oe,
  F as xe,
  x as _t,
  y as yt,
  z as ae,
  A as St,
  H as $e,
  J as wt,
  K as Pt,
  N as Ct,
  O as xt,
  U as bt,
  W as it,
  X as he,
  Y as a,
  Z as Mt,
  _ as q,
  $ as Se,
  a0 as J,
  a1 as Pe,
  a2 as It,
} from "../assets/vendor.js";
window.Vector3 = p;
(function () {
  const r = document.createElement("link").relList;
  if (r && r.supports && r.supports("modulepreload")) return;
  for (const o of document.querySelectorAll('link[rel="modulepreload"]')) t(o);
  new MutationObserver((o) => {
    for (const n of o)
      if (n.type === "childList")
        for (const l of n.addedNodes)
          l.tagName === "LINK" && l.rel === "modulepreload" && t(l);
  }).observe(document, { childList: !0, subtree: !0 });
  function e(o) {
    const n = {};
    return (
      o.integrity && (n.integrity = o.integrity),
      o.referrerPolicy && (n.referrerPolicy = o.referrerPolicy),
      o.crossOrigin === "use-credentials"
        ? (n.credentials = "include")
        : o.crossOrigin === "anonymous"
          ? (n.credentials = "omit")
          : (n.credentials = "same-origin"),
      n
    );
  }
  function t(o) {
    if (o.ep) return;
    o.ep = !0;
    const n = e(o);
    fetch(o.href, n);
  }
})();
class Tt {
  constructor() {
    s(this, "loading", 0);
    s(this, "velocity", 0);
    s(this, "accelerate", 1);
    s(this, "distance", 0);
    s(this, "movement", 0);
    s(this, "roughness", 0.07);
    s(this, "metalness", 0.04);
    s(this, "bodyColor", new P("#dbdbdb"));
    s(this, "normalScale", new $(0, 0));
    s(this, "interiorColor", new P("#ffd6b3"));
    s(this, "interiorFrameColor", new P("#0d0d0d"));
    s(this, "lightMapIntensity", 1);
    s(this, "motion");
    s(this, "freeCamera");
    s(this, "boxProjection");
    s(this, "environment");
    s(this, "bloom");
    s(this, "sharedFloor");
    s(this, "sharedUnifoms", {
      uVmin: { value: 1e3 },
      uTime: { value: 0 },
      uDistance: { value: 0 },
      reflectMap: { value: null },
      textureMatrix: { value: new He() },
      uInteriorLightColor: { value: new P("#ffffff") },
    });
    s(this, "c16Uniforms", {
      uDissolvePercent: { value: 0 },
      uDissolveBox: { value: 0 },
      uInsideLightMapIntensity: { value: 1 },
      uCarLightIntensity: { value: 20 },
    });
    s(this, "showButton", !0);
  }
}
const i = window.i = new Tt();
let Ke = new URLSearchParams(window.location.search).get("b");
Ke && Ke === "0" && (i.showButton = !1);
class Lt extends Ye {
  constructor() {
    super(...arguments);
    s(this, "extension", ["bin"]);
  }
  load({ url: e, onLoad: t, onProgress: o, onError: n }) {
    const l = (d) => {
      const C = d.scene;
      (d.animations && (C.animations = d.animations), t(Ye._setUserData(C)));
    };
    let m = new Dt(this.manager.loadingManager);
    (m.setPath(this.manager.path),
      m.setResourcePath(this.manager.resourcePath));
    let h = new lt(this.manager.loadingManager);
    (h.setDecoderPath(this.manager.dracoPath),
      m.setDRACOLoader(h),
      m.setMeshoptDecoder(ct),
      m.load(e, l, o, n));
  }
}
const Et = 255,
  Nt = "glTF",
  be = 12;
function Ze(c, r) {
  for (let e = 0, t = c.length; e < t; e++) c[e] ^= r;
  return c;
}
function At(c, r = Et) {
  Ze(new Uint8Array(c, 0, 4), r);
  const e = new DataView(c, 0, be),
    o = {
      magic: new TextDecoder().decode(new Uint8Array(c.slice(0, 4))),
      version: e.getUint32(4, !0),
      length: e.getUint32(8, !0),
    };
  if (o.magic !== Nt) throw new Error("Unsupported glTF-Binary header.");
  if (o.version < 2) throw new Error("Legacy binary file detected.");
  const n = o.length - be,
    l = new DataView(c, be);
  let m = 0;
  for (; m < n; ) {
    const h = l.getUint32(m, !0);
    ((m += 4), Ze(new Uint8Array(c, be + m, h + 4), r), (m += 4), (m += h));
  }
  return c;
}
class Dt extends mt {
  parse(r, e, t, o) {
    super.parse(At(r), e, t, o);
  }
}
const Bt = "1.0.5",
  Qe = {
    version: Bt,
    "textures/t_linearFiliter.png": "48fea4ca414ea0739e20ae9cbd73d33b.webp",
    "textures/t_hulan_bc.jpg": "46fbacd5c294eabbfddc3eb4cd52cbc2.webp",
    "textures/t_compass.jpg": "15fe58e22eadf47e4d2ee30a1d1dee29.webp",
    "textures/t_cell.png": "6a6f7dd5509f0263e5529da0f89925b7.webp",
    "textures/t_buildinglight.png": "608358b6c3ce912186d5b13121503f90.webp",
    "textures/point.png": "f385618308132881f73783eb1fb47a34.webp",
    "textures/lm_road.jpg": "24a4835873a17ee843f7950becce870f.webp",
    "textures/lm_inside_floor.jpg": "a9aafc528e17ca8fd239200518ddae5f.webp",
    "textures/lm_floor.jpg": "4d87c6abcc1039ee57cf9c5baab81f82.webp",
    "textures/lm_c16_outside.jpg": "78a02db3c277fa811507ee7b9a5b6ff6.webp",
    "textures/cell.png": "6a6f7dd5509f0263e5529da0f89925b7.webp",
    "textures/ao_floor.jpg": "17e193c69a2c1d3e6e945845dc0775e0.webp",
    "models/sm_scene01_reflectplane_dynamic.glb":
      "dbe250d8cf03649ad2f25447a3ea8851.bin",
    "models/sm_scene02.glb": "435661c4317bd0afb932b21a6337641b.bin",
    "models/sm_scene01_building_dynamic.glb":
      "28b5b06328827ac62527f6a2910ae06a.bin",
    "models/sm_c16simple.glb": "ee83957dcb8a14da6603b12ffe59a171.bin",
    "icons/p5.png": "9593a6760bb348f5b7e9bd6355bcd81a.webp",
    "icons/p4.png": "59a4a28d164e9fce1e58097c949b7621.webp",
    "icons/p3.png": "cf0b9fa6e19926b2341a308670107fa0.webp",
    "icons/p2.png": "93e2bf296a9f0c87021d5891a5c2b9d4.webp",
    "icons/p1.png": "c4f0e96747ac1b9aa97230f3b6d0852d.webp",
    "icons/p0.png": "fd1df33ef7ce36f7243d7586ec9d4f84.webp",
    "models/sm_scene01.glb": "93fcce08ce38a8b53f6d746843053677.bin",
    "models/sm_scene01_lights.glb": "dbe250d8cf03649ad2f25447a3ea8851.bin",
    "icons/Logo.png": "8e239ce2acc253ff5b9c02b591b4f756.webp",
    "textures/lm_c16_inside.jpg": "30fc714eec488627d1b3024ef238237a.webp",
    "textures/ao_c16_outside.jpg": "dd24dea58b5079f81fdeea068d4e8bef.webp",
    "textures/ao_c16_inside.jpg": "372a6943caf87c96e0ac677195b8ceb0.webp",
    "models/sm_scene02_dynamic.glb": "6cd80fcea4413720712cd2751432a046.bin",
    "environments/env_home.hdr": "994f57dc79a9ecc1778922d7d8d64f48.hdr",
    "textures/t_road_roughness.jpg": "2776da30d1ae61be26d03bfac65e2913.webp",
    "textures/t_road_bc.jpg": "6b0e0438ab529acbe6c23eaa40c60f5e.webp",
    "models/sm_scene03_props.glb": "a356fcaf059d931b2ebac4c6155d7363.bin",
    "models/sm_scene01_dynamic.glb": "4b11e57b74c91cbba272e30e9021f84c.bin",
    "textures/t_floor_roughness.jpg": "f6fb36baf16f107332e0f3e2bcefbbeb.webp",
    "models/sm_c16.glb": "2926a2be5e8e3208b0331b8a594b80d8.bin",
  },
  ot = "",
  Ot = (ot + "res/release/").replace("//", "/"),
  Ut = (ot + "res/res/").replace("//", "/");
class kt {
  constructor() {
    s(this, "codeVersion", "1.0.30");
    s(this, "compress", !0);
    s(this, "dracoPath", baseURL + "/draco/");
  }
  get DEBUG() {
    return (
      location.href.includes("localhost") || location.href.includes("192.168")
    );
  }
  get VERSION() {
    return `res ${Qe.version} code ${this.codeVersion}`;
  }
  sURL(r) {
    return this.compress ? Ot + Qe[r] : Ut + r;
  }
}
const we = new kt();
const Oe = we.sURL.bind(we),
  ye = class ye {
    static loadAsset(r, e, t = e.url) {
      return (
        (e.url = Oe(e.url)),
        r.loadAsset(e).then((o) => (ye.resource.set(t, o), o))
      );
    }
    static getRes(r) {
      return ye.resource.get(r);
    }
  };
s(ye, "resource", new Map());
let Ne = ye;
const y = Ne.getRes,
  T = Ne.loadAsset,
  le = class le {
    static get activeSceneName() {
      return le._activeSceneName;
    }
    static clear() {
      (this._scenesMap.clear(), (this._activeScene = null));
    }
    static getScene(r) {
      return this._scenesMap.get(r);
    }
    static registerScene(r, e) {
      return (this._scenesMap.set(r, e), e);
    }
    static prepareScene(r) {
      const e = r || this._scenesMap.keys(),
        t = [];
      for (let o of e) {
        let n = this._scenesMap.get(o);
        n &&
          !n.flags.OnPreCalled &&
          ((n.flags.OnPreCalled = !0), n.onPre && t.push(n.onPre()));
      }
      return Promise.all(t);
    }
    static runScene(r, e = {}) {
      let t = this._scenesMap.get(r);
      if (t) {
        let o = this._activeScene;
        (o && o.onExit && o.onExit(e),
          t.flags.onInitCalled ||
            ((t.flags.onInitCalled = !0), t.onInit && t.onInit()),
          t.onEnter && t.onEnter(e),
          (this._activeScene = t),
          (this._activeSceneName = r));
      }
    }
    static updateScene(r) {
      this._scenesMap.forEach((e) => {
        e.onUpdate && e.onUpdate(r);
      });
    }
  };
(s(le, "_scenesMap", new Map()),
  s(le, "_activeScene", null),
  s(le, "_activeSceneName", ""));
let N = le;
const re = {
  HomeScene: {
    envMapIntensity: 1,
    boxProjection: {
      boxMin: new p(-2.5, 0, -4.3),
      boxMax: new p(2.5, 2.5, 3.3),
    },
  },
  HomeCity: {
    envMapIntensity: 0.5,
    boxProjection: { boxMin: new p(-2.5, 0, -8), boxMax: new p(2.5, 2.5, 8) },
  },
  AIDriveScene: {
    envMapIntensity: 1,
    boxProjection: {
      boxMin: new p(-2.5, 0, -2.5),
      boxMax: new p(2.5, 2.5, 2.5),
    },
  },
  AIDriveRadar: {
    envMapIntensity: 0.3,
    boxProjection: {
      boxMin: new p(-2.5, 0, -2.5),
      boxMax: new p(2.5, 2.5, 2.5),
    },
  },
  InteriorScene: {
    envMapIntensity: 0.7,
    boxProjection: {
      boxMin: new p(-2.5, 0, -2.5),
      boxMax: new p(2.5, 2.5, 2.5),
    },
  },
  InteriorDetail: {
    envMapIntensity: 1,
    boxProjection: {
      boxMin: new p(-2.5, 0, -2.5),
      boxMax: new p(2.5, 2.5, 2.5),
    },
  },
};
class Ft {
  constructor(r) {
    s(this, "flags", {});
    this.viewer = r;
  }
}
class ve extends Ft {
  constructor(r, e) {
    (super(r), (this.viewer = r), (this.config = e));
  }
  onEnter(r) {
    const e = this.viewer,
      t = this.config,
      o = { easing: j.Cubic.InOut };
    (e.killTweensOf(i.environment),
      e
        .timeline(i.environment)
        .to({ envMapIntensity: t.envMapIntensity }, 2, o)
        .start(),
      e.killTweensOf(i.boxProjection),
      e
        .timeline(i.boxProjection)
        .to(
          { boxMin: t.boxProjection.boxMin, boxMax: t.boxProjection.boxMax },
          2,
          o,
        )
        .start());
  }
}
const F = { DEFAULT: 1, PLANE_REFLECT: 2, ENV_REFLECT: 4 },
  te = { BEGIN: 0, RESET: 1 };
class zt extends Ae {
  constructor() {
    super(...arguments);
    s(this, "_wheels", []);
    s(this, "_velocity", 0);
  }
  onLoad() {
    ((this._wheels[0] = _.getChildByName(this.node, "wheel")),
      (this._wheels[1] = _.getChildByName(this.node, "wheel001")));
  }
  update(e) {
    ((this._velocity = De.lerp(this._velocity, i.velocity, e * i.accelerate)),
      this._wheels.forEach((o) => {
        o.rotateZ(((this._velocity * e) / (Math.PI * 0.757)) * 2 * Math.PI);
      }));
    const t = this._velocity * e;
    ((i.movement += t), (i.sharedUnifoms.uDistance.value += t));
  }
}
ge.noise = `

float random21(vec2 st) {
    return fract(sin(dot(st.xy, vec2(12.9898,78.233))) * 43758.5453123);
}

vec2 random2( vec2 p ) {
    p = vec2(dot(p,vec2(127.1,311.7)), dot(p,vec2(269.5,183.3)));
    return -1.0 + 2.0*fract(sin(p)*43758.5453123);
}

float random31(vec3 co) {
    return fract(sin(dot(co.xyz, vec3(12.9898, 78.233, 126.7378))) * 43758.5453);
}

vec3 random3(vec3 c) {
	float j = 4096.0*sin(dot(c,vec3(17.0, 59.4, 15.0)));
	vec3 r;
	r.z = fract(512.0*j);
	j *= .125;
	r.x = fract(512.0*j);
	j *= .125;
	r.y = fract(512.0*j);
	return r-0.5;
}

const float F3 =  0.3333333;
const float G3 =  0.1666667;

float noise3d(vec3 p) {
    vec3 s = floor(p + dot(p, vec3(F3)));
    vec3 x = p - s + dot(s, vec3(G3));

    vec3 e = step(vec3(0.0), x - x.yzx);
    vec3 i1 = e*(1.0 - e.zxy);
    vec3 i2 = 1.0 - e.zxy*(1.0 - e);
   
    vec3 x1 = x - i1 + G3;
    vec3 x2 = x - i2 + 2.0*G3;
    vec3 x3 = x - 1.0 + 3.0*G3;
    
    vec4 w, d;
    
    w.x = dot(x, x);
    w.y = dot(x1, x1);
    w.z = dot(x2, x2);
    w.w = dot(x3, x3);
    
    w = max(0.6 - w, 0.0);
    
    d.x = dot(random3(s), x);
    d.y = dot(random3(s + i1), x1);
    d.z = dot(random3(s + i2), x2);
    d.w = dot(random3(s + 1.0), x3);
    
    w *= w;
    w *= w;
    d *= w;
    
    return dot(d, vec4(52.0));
}

float noise2d( in vec2 p ) {
    const float K1 = 0.366025404; // (sqrt(3)-1)/2;
    const float K2 = 0.211324865; // (3-sqrt(3))/6;
    vec2 i = floor(p + (p.x+p.y)*K1);	
    vec2 a = p - i + (i.x+i.y)*K2;
    vec2 o = (a.x>a.y) ? vec2(1.0,0.0) : vec2(0.0,1.0); //vec2 of = 0.5 + 0.5*vec2(sign(a.x-a.y), sign(a.y-a.x));
    vec2 b = a - o + K2;
    vec2 c = a - 1.0 + 2.0*K2;
    vec3 h = max(0.5-vec3(dot(a,a), dot(b,b), dot(c,c) ), 0.0 );
    vec3 n = h*h*h*h*vec3( dot(a,random2(i+0.0)), dot(b,random2(i+o)), dot(c,random2(i+1.0)));
    return dot(n, vec3(70.0));	
}
`;
ge.sdf = `

float sdSphere( vec3 p, float s ) {
  return length(p)-s;
}

float sdBox( vec3 p, vec3 b ) {
  vec3 q = abs(p) - b;
  return length(max(q,0.0)) + min(max(q.x,max(q.y,q.z)),0.0);
}

float sdRoundBox( vec3 p, vec3 b, float r ) {
  vec3 q = abs(p) - b + r;
  return length(max(q,0.0)) + min(max(q.x,max(q.y,q.z)),0.0) - r;
}

float sdBoxFrame( vec3 p, vec3 b, float e ) {
       p = abs(p  )-b;
  vec3 q = abs(p+e)-e;
  return min(min(
      length(max(vec3(p.x,q.y,q.z),0.0))+min(max(p.x,max(q.y,q.z)),0.0),
      length(max(vec3(q.x,p.y,q.z),0.0))+min(max(q.x,max(p.y,q.z)),0.0)),
      length(max(vec3(q.x,q.y,p.z),0.0))+min(max(q.x,max(q.y,p.z)),0.0));
}

float sdTorus( vec3 p, vec2 t ) {
  vec2 q = vec2(length(p.xz)-t.x,p.y);
  return length(q)-t.y;
}

float sdCappedTorus( vec3 p, vec2 sc, float ra, float rb) {
  p.x = abs(p.x);
  float k = (sc.y*p.x>sc.x*p.y) ? dot(p.xy,sc) : length(p.xy);
  return sqrt( dot(p,p) + ra*ra - 2.0*ra*k ) - rb;
}

float sdLink( vec3 p, float le, float r1, float r2 ) {
  vec3 q = vec3( p.x, max(abs(p.y)-le,0.0), p.z );
  return length(vec2(length(q.xy)-r1,q.z)) - r2;
}

float sdPlane( vec3 p, vec3 n, float h ) {
  // n must be normalized
  return dot(p,n) + h;
}
`;
const _e = `
varying vec2 vUv;
varying vec3 vPositionWS;

void main() {
    vUv = uv;

    vec4 transformed = vec4(position, 1.);

#ifdef USE_INSTANCING
    transformed = instanceMatrix * transformed;
#endif

    vec4 positionWS = modelMatrix * transformed;
    vec4 mvPosition = viewMatrix * positionWS;

    gl_Position = projectionMatrix * mvPosition;
    vPositionWS = positionWS.xyz;
}
`,
  Fe = `
uniform vec3      uColor;
uniform float     uPercent;
uniform float     uIntensity;
varying vec2      vUv;
varying vec3      vPositionWS;

#ifdef USE_MASK
    uniform sampler2D tMask;
#endif

#ifdef USE_MASK_TRANSPARENT
    uniform sampler2D tMask;
#endif

#ifdef USE_AOMAP
    uniform sampler2D aoMap;
#endif 

void main() {
    if (vUv.x > uPercent) discard;

    #ifdef USE_MASK
        float mask = texture(tMask, vUv).r;
        if(mask < 0.5) discard;
    #endif

    vec3 color = uColor * uIntensity;
    gl_FragColor = vec4(color, 1.);

    #ifdef USE_MASK_TRANSPARENT
        gl_FragColor.a = texture(tMask, vUv).r;
    #endif

#ifdef USE_AOMAP
    gl_FragColor.rgb *= texture2D(aoMap, vec2(vPositionWS.x, -vPositionWS.z) / 3.97104 * 0.5 + 0.5).r;
#endif

    #include <tonemapping_fragment>
    #include <colorspace_fragment>
}
`,
  Rt = `
uniform sampler2D tAlpha;
uniform vec3      uColor;
uniform float     uPercent;
uniform float     uIntensity;
varying vec2      vUv;

void main() {
    if (vUv.x > uPercent) discard;

    float alpha = texture2D(tAlpha, vUv).r;
    gl_FragColor = vec4(uColor * uIntensity, alpha);

    #include <tonemapping_fragment>
    #include <colorspace_fragment>
}
`;
function Wt(c) {
  (ge.worldpos_vertex.indexOf("USE_WORLD_POS") === -1 &&
    (ge.worldpos_vertex = ge.worldpos_vertex.replace(
      "#if defined( USE_ENVMAP )",
      "#if defined( USE_ENVMAP ) || defined( USE_WORLD_POS )",
    )),
    c.vertexShader.indexOf("vPositionWS") === -1 &&
      (c.vertexShader = c.vertexShader
        .replace(
          "#include <common>",
          `
        #define USE_WORLD_POS
        #include <common>
        `,
        )
        .replace(
          "void main() {",
          `
        varying vec3 vPositionWS;
        void main() {
        `,
        )
        .replace(
          "#include <worldpos_vertex>",
          `
        #include <worldpos_vertex>
        vPositionWS = worldPosition.xyz;
        `,
        )),
    c.fragmentShader.indexOf("vPositionWS") === -1 &&
      (c.fragmentShader = c.fragmentShader.replace(
        "void main() {",
        `
        varying vec3 vPositionWS;
        void main() {
        `,
      )));
}
function Me(c, r, e) {
  (Wt(c),
    (c.fragmentShader = c.fragmentShader
      .replace(
        "void main() {",
        `
            #include <noise>
            #include <sdf>
            
            #define N_CELL 8.
            uniform float uPercent;
            uniform float uBox;

            float getDissolveMask() {
                float x = mix(-6., 3., uPercent);
                vec3  posW = floor(vPositionWS * N_CELL) / N_CELL;
                float offsetX = 1. - clamp(posW.x - x, 0., 1.25) * 0.8;

                vec3  cellPos = vPositionWS * N_CELL;
                float rr01 = random21(vec2(floor(cellPos.x), 1.));
                float rr02 = random21(vec2(floor(cellPos.z), 1.));
                float mask = abs(random21(vec2(floor(cellPos.y) + rr01 + rr02, 1.)));
            
                float cx = fract(cellPos.x);
                float cy = fract(cellPos.y);
                float cz = fract(cellPos.z);
                vec3 inPos = abs(vec3(cx, cy, cz) - vec3(0.5)) * 2.;
                float inDistance = sdBox(inPos, vec3(1.)) * 0.5;
            
                mask = abs(mask - inDistance);
                mask += offsetX * 2. - 1.;
                mask = clamp(mask, 0., 1.);

                return mask;
            }

            void main() {
                float mask = getDissolveMask();
            #ifdef USE_AOMAP
                mask = mix(1., mask , mix(1., step(0.2, texture2D( aoMap, vAoMapUv ).g), uBox));
            #endif 
                if (mask < 0.9) discard;
        `,
      )
      .replace(
        "#include <opaque_fragment>",
        `
            outgoingLight = mix(vec3(5., 12., 6.) * 0.2, outgoingLight, vec3(1. - step(mask, 0.95)));
            #include <opaque_fragment>
        `,
      )),
    (c.uniforms.uBox = e),
    (c.uniforms.uPercent = r));
}
function Je(c, r) {
  ((c.vertexShader = c.vertexShader.replace(
    "void main() {",
    `
        varying vec3 vNormalW;
        varying vec3 vPositionW;
        void main() {
            vPositionW = vec3( modelMatrix*vec4( position, 1.0 ));
            vNormalW = normalize( vec3( vec4( normal, 0.0 ) * modelMatrix ) );
        `,
  )),
    (c.fragmentShader = c.fragmentShader
      .replace(
        "void main() {",
        `
            uniform vec3 uLightColor;
            varying vec3 vNormalW;
            varying vec3 vPositionW;
            void main() {
            `,
      )
      .replace(
        "vec4 lightMapTexel = texture2D( lightMap, vLightMapUv );",
        `
            vec4 lightMapTexel = texture2D( lightMap, vLightMapUv );
            vec3 viewDirectionW = normalize(cameraPosition - vPositionW);
            float fresnelTerm = dot(viewDirectionW, vNormalW);
            fresnelTerm = clamp(1.0 - fresnelTerm, 0., 1.);
            // fresnelTerm = pow(fresnelTerm,2.);
            lightMapTexel.rgb*=mix(vec3(1.),uLightColor,vec3(fresnelTerm*smoothstep(0.8,1.1,vPositionW.y)));

            `,
      )),
    (c.uniforms.uLightColor = r));
}
const Vt = `
#include <noise>
attribute vec3 spritePos;

uniform float uPercent;
varying vec2  vUv;
varying float vPosX;
varying float vOffsetX;
varying vec3  vPositionWS;

void main() {
    vec4 transformed = vec4(position, 1.0);

    vPositionWS = vec3(modelMatrix * transformed);
    vUv = uv;

    vPosX = mix(-6., 3., uPercent) + abs(random21(spritePos.xz)) * 2.2;
    vOffsetX = 1. - clamp(spritePos.x - vPosX, 0., 1.);

    vec3 dir = normalize(mix(normalize(normal), normalize(cameraPosition - vec3(0.,1.5,0.)), 0.95));
    transformed.xyz += dir * length(cameraPosition) * (1. - vOffsetX);

    gl_Position = projectionMatrix * modelViewMatrix * transformed;
}`,
  Ht = `
#include <sdf>

uniform sampler2D tMask;
uniform float     uPercent;
uniform float     uBox;

varying vec2      vUv;
varying float     vPosX;
varying float     vOffsetX;
varying vec3      vPositionWS;

void main() {
    float mask = texture(tMask, vUv).r;

    if (mask > vOffsetX || 
        mask < vOffsetX - 0.1 || 
        vPosX > vPositionWS.x + 0.5
    ) {
        discard;
    }

    gl_FragColor = vec4(vec3(0.5, 1.2, 0.8), 1.);

    #include <tonemapping_fragment>
    #include <colorspace_fragment>
}`,
  ce = class ce extends Ge {
    constructor(r) {
      super();
      const e = r.geometry.attributes.position.array,
        t = r.geometry.index.array,
        o = t.length,
        n = new Float32Array(o * 3),
        l = new Float32Array(o * 3),
        m = new Float32Array(o * 3),
        h = new Float32Array(o * 2),
        d = new Uint16Array(o),
        { __face: C, __normal: x, __position: L } = ce;
      for (let f = 0, B = t.length / 3; f < B; f++) {
        const H = f * 3,
          u = f * 9,
          X = f * 6;
        ((d[H] = H),
          (d[H + 1] = H + 1),
          (d[H + 2] = H + 2),
          (n[u] = e[t[H] * 3]),
          (n[u + 1] = e[t[H] * 3 + 1]),
          (n[u + 2] = e[t[H] * 3 + 2]),
          (n[u + 3] = e[t[H + 1] * 3]),
          (n[u + 4] = e[t[H + 1] * 3 + 1]),
          (n[u + 5] = e[t[H + 1] * 3 + 2]),
          (n[u + 6] = e[t[H + 2] * 3]),
          (n[u + 7] = e[t[H + 2] * 3 + 1]),
          (n[u + 8] = e[t[H + 2] * 3 + 2]),
          C.a.set(n[u], n[u + 1], n[u + 2]),
          C.b.set(n[u + 3], n[u + 4], n[u + 5]),
          C.c.set(n[u + 6], n[u + 7], n[u + 8]),
          C.getNormal(x),
          (l[u] = x.x),
          (l[u + 1] = x.y),
          (l[u + 2] = x.z),
          (l[u + 3] = x.x),
          (l[u + 4] = x.y),
          (l[u + 5] = x.z),
          (l[u + 6] = x.x),
          (l[u + 7] = x.y),
          (l[u + 8] = x.z),
          C.getMidpoint(L),
          (m[u] = L.x),
          (m[u + 1] = L.y),
          (m[u + 2] = L.z),
          (m[u + 3] = L.x),
          (m[u + 4] = L.y),
          (m[u + 5] = L.z),
          (m[u + 6] = L.x),
          (m[u + 7] = L.y),
          (m[u + 8] = L.z),
          (h[X] = 0),
          (h[X + 1] = 0),
          (h[X + 2] = 1),
          (h[X + 3] = 0),
          (h[X + 4] = 0),
          (h[X + 5] = 1));
      }
      const I = new ee(n, 3),
        M = new ee(l, 3),
        A = new ee(m, 3),
        D = new ee(h, 2),
        W = new Be();
      (W.setAttribute("position", I),
        W.setAttribute("normal", M),
        W.setAttribute("spritePos", A),
        W.setAttribute("uv", D),
        (W.index = new ee(d, 1)),
        (this.geometry = W),
        (this.material = new Z({
          name: "mStartParticles",
          vertexShader: Vt,
          fragmentShader: Ht,
          uniforms: { tMask: { value: r.tMask }, uPercent: r.uPercent },
        })));
    }
  };
(s(ce, "__face", new dt()),
  s(ce, "__normal", new p()),
  s(ce, "__position", new p()));
let ze = ce;
class Gt extends ve {
  constructor() {
    super(...arguments);
    s(this, "_root");
    s(this, "_uniforms", {
      uColor: { value: new P(0) },
      uHomePercent: { value: 0 },
      uLogoPercent: { value: 0 },
      uStartPercent: { value: 0 },
      uReflectIntensity: { value: 0 },
    });
    s(this, "_mInside");
    s(this, "_mCarLight");
  }
  onPre() {
    const e = this.viewer;
    return Promise.all([
      T(e, { url: "models/sm_c16.glb" }, "c16"),
      T(e, { url: "models/sm_c16simple.glb" }, "c16simple"),
      T(
        e,
        {
          url: "textures/ao_c16_outside.jpg",
          flipY: !1,
          channel: 1,
          format: Xe,
          colorSpace: Te,
        },
        "ao_c16_outside",
      ),
      T(
        e,
        {
          url: "textures/ao_c16_inside.jpg",
          flipY: !1,
          channel: 1,
          format: Xe,
          colorSpace: Te,
        },
        "ao_c16_inside",
      ),
      T(
        e,
        { url: "textures/lm_c16_outside.jpg", flipY: !1, channel: 1 },
        "lm_c16_outside",
      ),
      T(
        e,
        { url: "textures/lm_c16_inside.jpg", flipY: !1, channel: 1 },
        "lm_c16_inside",
      ),
      T(e, { url: "textures/t_cell.png" }, "t_cell"),
    ]);
  }
  onInit() {
    const e = new Z({
        name: "mLogo",
        vertexShader: _e,
        fragmentShader: Fe,
        uniforms: {
          uColor: { value: new P(9764763) },
          uIntensity: { value: 1.4 },
          uPercent: this._uniforms.uLogoPercent,
        },
        toneMapped: !1,
      }),
      t = new Z({
        name: "mLogo",
        vertexShader: _e,
        fragmentShader: Fe,
        uniforms: {
          uColor: { value: new P(9764763) },
          uIntensity: { value: 0.25 },
          uPercent: this._uniforms.uLogoPercent,
        },
        toneMapped: !1,
      }),
      o = this.viewer;
    this._root = o.addNode(ue, { name: "HomeScene" });
    const n = y("c16simple");
    o.addNode(
      new ze({
        geometry: _.getMesh(n)[0].geometry,
        tMask: y("t_cell"),
        uPercent: this._uniforms.uStartPercent,
      }),
    );
    const l = y("sm_floor"),
      m = _.findChild(l, "logo002");
    o.addNode(m, {
      parent: this._root,
      material: e,
      layer: F.DEFAULT | F.ENV_REFLECT | F.PLANE_REFLECT,
    });
    const h = _.findChild(l, "cemian");
    o.addNode(h, { material: t, parent: this._root, layer: F.ENV_REFLECT });
    const d = y("c16");
    o.addNode(d, { component: new zt() });
    const C = _.getObject(_.getChildByName(d, "out"), (f) => f.isMesh, !0),
      x = _.getObject(_.getChildByName(d, "wheel"), (f) => f.isMesh, !0),
      L = _.getObject(_.getChildByName(d, "wheel001"), (f) => f.isMesh, !0);
    [...C, ...x, ...L].forEach((f) => {
      ((f.layers.mask = F.DEFAULT | F.PLANE_REFLECT),
        (f.material.aoMap = y("ao_c16_outside")));
    });
    const I = _.getMaterial(d, "m_c16_body");
    ((I.roughness = i.roughness),
      (I.metalness = i.metalness),
      (I.color = i.bodyColor.convertLinearToSRGB().clone()),
      (I.normalScale = i.normalScale.clone()),
      (I.normalMap.anisotropy = 8));
    const M = new se();
    ((M.color = new P("#ffd6b3").convertLinearToSRGB()),
      (M.name = "mInside"),
      (M.map = _.getTexture(d, "t_insidecolor")),
      (M.aoMap = y("ao_c16_inside")),
      (M.lightMap = y("lm_c16_inside")),
      (M.onBeforeCompile = (f) => {
        (Me(f, i.c16Uniforms.uDissolvePercent, i.c16Uniforms.uDissolveBox),
          Je(f, i.sharedUnifoms.uInteriorLightColor));
      }));
    const A = M.clone();
    ((A.color = new P("#0d0d0d").convertLinearToSRGB()),
      (A.name = "mInsideFrame"),
      (A.lightMapIntensity = 3),
      (A.onBeforeCompile = (f) => {
        (Me(f, i.c16Uniforms.uDissolvePercent, i.c16Uniforms.uDissolveBox),
          Je(f, i.sharedUnifoms.uInteriorLightColor));
      }));
    const D = new se();
    ((D.name = "mInstrument"),
      (D.map = _.getTexture(d, "t_screen")),
      (D.aoMap = y("ao_c16_inside")),
      (D.onBeforeCompile = (f) => {
        Me(f, i.c16Uniforms.uDissolvePercent, i.c16Uniforms.uDissolveBox);
      }),
      Object.assign(d.userData.materials, { mInside: M, mInsideFrame: A }));
    const W = _.getChildByName(d, "in");
    (_.getObject(W, (f) => f.isMesh && f.visible, !0).forEach((f) => {
      f.material.name === "m_in_second"
        ? (f.material = A)
        : f.material.name === "m_screen"
          ? (f.material = D)
          : (f.material = M);
    }),
      _.getMaterial(d).forEach((f) => {
        f.isMeshStandardMaterial &&
          (f.onBeforeCompile = (B) => {
            (i.boxProjection.useBoxProjection(B),
              Me(
                B,
                i.c16Uniforms.uDissolvePercent,
                i.c16Uniforms.uDissolveBox,
              ));
          });
      }),
      (_.getMaterial(d, "m_glass").opacity = 0.9),
      (this._mInside = M),
      (this._mCarLight = _.getMaterial(d, "m_carlight")));
  }
  onUpdate(e) {
    (this._mInside &&
      (this._mInside.lightMapIntensity =
        i.c16Uniforms.uInsideLightMapIntensity.value),
      this._mCarLight &&
        (this._mCarLight.emissiveIntensity =
          i.c16Uniforms.uCarLightIntensity.value));
  }
  onEnter(e) {
    (super.onEnter(e), (this._root.visible = !0));
    const t = this.viewer;
    e.action === te.BEGIN
      ? (t.killTweensOf(this._uniforms.uStartPercent),
        t
          .timeline(this._uniforms.uStartPercent)
          .delay(2)
          .to({ value: 1 }, 4)
          .start(),
        t.killTweensOf(i.c16Uniforms.uDissolvePercent),
        t
          .timeline(i.c16Uniforms.uDissolvePercent)
          .delay(2)
          .to({ value: 1 }, 4)
          .start(),
        t.killTweensOf(this),
        t
          .timeline(this)
          .call(() => {
            i.freeCamera.gotoPOI({ duration: 2 });
          })
          .delay(2)
          .call(() => {
            i.freeCamera.gotoPOI({
              fov: 33.4,
              springLength: 8,
              lookAt: new p(0, 1, 0),
              duration: 3,
            });
          })
          .start(),
        (i.sharedFloor.visible = !0),
        t.killTweensOf(i.sharedFloor.material),
        t
          .timeline(i.sharedFloor.material)
          .delay(2)
          .to({ color: new P(16777215), alpha: 1, reflectIntensity: 1 }, 3, {
            easing: j.Cubic.InOut,
          })
          .start(),
        t.killTweensOf(this._uniforms.uLogoPercent),
        t
          .timeline(this._uniforms.uLogoPercent)
          .delay(1)
          .to({ value: 1 }, 2.5, { easing: j.Cubic.InOut })
          .start())
      : (e.action === te.RESET
          ? (i.freeCamera.gotoPOI({
              fov: 33.4,
              springLength: 8,
              lookAt: new p(0, 1, 0),
              phi: Math.PI / 2,
              theta: 0,
              smoothing: 3,
            }),
            (i.freeCamera.phiMin = 0.5),
            (i.freeCamera.phiMax = Math.PI / 2))
          : (i.freeCamera.gotoPOI({
              fov: 33.4,
              springLength: 8,
              lookAt: new p(0, 1, 0),
              smoothing: 3,
            }),
            (i.freeCamera.phiMin = 0.5),
            (i.freeCamera.phiMax = Math.PI / 2)),
        (i.sharedFloor.visible = !0),
        t.killTweensOf(i.sharedFloor.material),
        t
          .timeline(i.sharedFloor.material)
          .to({ color: new P(16777215), alpha: 1, lightMapBlending: 0 }, 1)
          .start(),
        t.killTweensOf(this._uniforms.uLogoPercent),
        t.timeline(this._uniforms.uLogoPercent).to({ value: 1 }, 1).start(),
        (i.velocity = 0),
        (i.accelerate = 1.5));
  }
  onExit() {
    const e = this.viewer;
    (e.killTweensOf(i.sharedFloor.material),
      e
        .timeline(i.sharedFloor.material)
        .to({ color: new P(0), alpha: 0 }, 2)
        .call(() => {
          i.sharedFloor.visible = !1;
        })
        .start(),
      e.killTweensOf(this._uniforms.uLogoPercent),
      e
        .timeline(this._uniforms.uLogoPercent)
        .to({ value: 0 }, 1)
        .delay(1)
        .call(() => {
          this._root.visible = !1;
        })
        .start());
  }
}
var jt = Object.defineProperty,
  qt = Object.getOwnPropertyDescriptor,
  Ce = (c, r, e, t) => {
    for (var o = qt(r, e), n = c.length - 1, l; n >= 0; n--)
      (l = c[n]) && (o = l(r, e, o) || o);
    return (o && jt(r, e, o), o);
  };
const Yt = `
uniform mat4 textureMatrix;
varying vec4 vUv4;
varying vec2 vUv;
varying vec3 vPositionWS;

#ifdef USE_UV1
    varying vec2 vUv1;
#endif

#ifdef USE_UV2
    varying vec2 vUv2;
#endif

#ifdef USE_UV3
    varying vec2 vUv3;
#endif

varying vec3 vViewPosition;

#include <common>
#include <normal_pars_vertex>
#include <logdepthbuf_pars_vertex>

void main() {
    #include <beginnormal_vertex>
    #include <defaultnormal_vertex>

    vec4 transformed = vec4(position, 1.);

#ifdef USE_INSTANCING
    transformed = instanceMatrix * transformed;
#endif

    vUv = uv;

#ifdef USE_UV1   
    vUv1 = uv1;
#endif

#ifdef USE_UV2   
    vUv2 = uv2;
#endif

#ifdef USE_UV3   
    vUv3 = uv2;
#endif

    vec4 positionWS = modelMatrix * transformed;
    vec4 mvPosition = viewMatrix * positionWS;

    vUv4 = textureMatrix * positionWS;
    vPositionWS = positionWS.xyz;
    vViewPosition = -mvPosition.xyz;
    vNormal = normalize(transformedNormal);

    gl_Position = projectionMatrix * mvPosition;

    #include <logdepthbuf_vertex>
}
`,
  Xt = `
uniform vec3      color;
uniform float     alpha;
uniform float     roughness;
uniform float     metalness;
uniform sampler2D map;
uniform sampler2D normalMap;
uniform sampler2D roughnessMap;
uniform sampler2D aoMap;
uniform float     aoMapIntensity;
uniform sampler2D lightMap;
uniform float     lightMapIntensity;
uniform sampler2D reflectMap;
uniform float     reflectIntensity;
uniform float     uPercent;
varying vec3      vPositionWS;

#ifdef USE_LIGHTMAP_BLENDING
uniform sampler2D lightMap2;
uniform float     lightMapBlending;
#endif

#ifdef ROUGHNESSMAP_WORLDPOS_SCALE
    uniform float uDistance;
#endif 

varying vec2 vUv;

#ifdef USE_UV1
    varying vec2 vUv1;
#endif

#ifdef USE_UV2
    varying vec2 vUv2;
#endif

#ifdef USE_UV3
    varying vec2 vUv3;
#endif

varying vec4 vUv4;
varying vec3 vViewPosition;
varying vec3 vNormal;

#include <common>
#include <lights_physical_pars_fragment>
#include <logdepthbuf_pars_fragment>

void main() {
    #include <logdepthbuf_fragment>

    vec3 coord = vUv4.xyz / vUv4.w;
    vec2 reflectUv = coord.xy;

#ifdef USE_NORMALMAP
    vec4 texelNormal = texture2D(normalMap, UV_NORMAL);
    vec3 normal = normalize(vec3(texelNormal.r * 2.0 - 1.0, texelNormal.b,  texelNormal.g * 2.0 - 1.0));
    reflectUv += coord.z * normal.xz * 0.05;
#endif

    vec3 geometryNormal = normalize(vNormal);
    vec3 geometryViewDir = normalize(vViewPosition);

    vec3 singleScattering = vec3( 0.0 );
	vec3 multiScattering = vec3( 0.0 );

    float metalnessFactor = metalness;

    vec4 diffuseColor = vec4(color, alpha);

#ifdef USE_MAP
    diffuseColor *= texture2D(map, vUv);
#endif

    diffuseColor.rgb = diffuseColor.rgb * ( 1.0 - metalnessFactor );

    vec3  specularColor = mix( vec3( 0.04 ), diffuseColor.rgb, metalnessFactor );
    float specularF90 = 1.0;

    float roughnessFactor = roughness;

#ifdef USE_ROUGHNESSMAP

#if defined(ROUGHNESSMAP_WORLDPOS_SCALE)
    roughnessFactor *= texture2D(roughnessMap, (vPositionWS.xz - vec2(uDistance, 0.)) * ROUGHNESSMAP_WORLDPOS_SCALE).g * roughness;
#else
    roughnessFactor *= texture2D(roughnessMap, UV_ROUGHNESS).g * roughness;
#endif

#endif

    computeMultiscattering( geometryNormal, geometryViewDir, specularColor, specularF90, roughnessFactor, singleScattering, multiScattering );

    vec3 totalScattering = singleScattering + multiScattering;
	vec3 diffuse = diffuseColor.rgb * ( 1.0 - max( max( totalScattering.r, totalScattering.g ), totalScattering.b ) );

    vec3 irradiance = vec3(0.);

#ifdef USE_LIGHTMAP

#ifdef USE_LIGHTMAP_BLENDING
    irradiance += mix(texture2D(lightMap, UV_LIGHTMAP).rgb, texture2D(lightMap2, UV_LIGHTMAP).rgb, lightMapBlending) * lightMapIntensity;
#else
    irradiance += texture2D(lightMap, UV_LIGHTMAP).rgb * lightMapIntensity;
#endif

#endif

    float lod = roughnessFactor * (1.7 - 0.7 * roughnessFactor) * 6.;
    vec4  reflectColor = textureLod(reflectMap, reflectUv, lod) * reflectIntensity;

    vec3 f_specular = vec3(0.);
    vec3 f_diffuse = vec3(0.);

    f_specular += reflectColor.rgb * (1.0 - roughnessFactor * roughnessFactor) + multiScattering * irradiance;
    f_diffuse += diffuse * irradiance;

#ifdef USE_AOMAP

#ifdef USE_AOMAP_WORLDPOS
    vec2  aoUv = vec2(vPositionWS.x, -vPositionWS.z) / 3.97104 * 0.5 + 0.5;
    float ambientOcclusion = mix(1., texture2D(aoMap, aoUv).r, aoMapIntensity);
#else
    float ambientOcclusion = mix(1., texture2D(aoMap, UV_AO).r, aoMapIntensity);
#endif

    float dotNV = saturate( dot( geometryNormal, geometryViewDir ) );
    f_specular *= computeSpecularOcclusion(dotNV, ambientOcclusion, roughnessFactor);
    f_diffuse *= ambientOcclusion;
#endif

    gl_FragColor = vec4((f_specular + f_diffuse)*uPercent, diffuseColor.a);

    #include <tonemapping_fragment>
    #include <colorspace_fragment>
}
`;
class ne extends Z {
  constructor() {
    super(...arguments);
    s(this, "vertexShader", Yt);
    s(this, "fragmentShader", Xt);
    s(this, "defines", {
      UV_AO: "vUv",
      UV_LIGHTMAP: "vUv",
      UV_ROUGHNESS: "vUv",
      UV_NORMAL: "vUv",
      USE_AOMAP_WORLDPOS: "",
    });
    s(this, "uniforms", {
      color: { value: new P() },
      alpha: { value: 1 },
      map: { value: null },
      reflectMap: { value: null },
      reflectIntensity: { value: 1 },
      textureMatrix: { value: new He() },
      metalness: { value: 0 },
      roughness: { value: 1 },
      roughnessMap: { value: null },
      normalMap: { value: null },
      aoMap: { value: null },
      aoMapIntensity: { value: 1 },
      lightMap: { value: null },
      lightMapIntensity: { value: 1 },
      lightMap2: { value: null },
      lightMapBlending: { value: 0 },
      uDistance: { value: null },
      uPercent: { value: 1 },
    });
  }
  onBeforeCompile(e) {
    const t = [];
    (this.aoMap && this.aoMap.channel && t.push(["UV_AO", this.aoMap.channel]),
      this.lightMap &&
        this.lightMap.channel &&
        t.push(["UV_LIGHTMAP", this.lightMap.channel]),
      this.roughnessMap &&
        this.roughnessMap.channel &&
        t.push(["UV_ROUGHNESS", this.roughnessMap.channel]),
      this.normalMap &&
        this.normalMap.channel &&
        t.push(["UV_NORMAL", this.normalMap.channel]),
      t.forEach(([o, n]) => {
        e.defines[o] = "vUv" + n;
      }));
  }
  get color() {
    return this.uniforms.color.value;
  }
  set color(e) {
    this.uniforms.color.value = e;
  }
  get alpha() {
    return this.uniforms.alpha.value;
  }
  set alpha(e) {
    this.uniforms.alpha.value = e;
  }
  get metalness() {
    return this.uniforms.metalness.value;
  }
  set metalness(e) {
    this.uniforms.metalness.value = e;
  }
  get roughness() {
    return this.uniforms.roughness.value;
  }
  set roughness(e) {
    this.uniforms.roughness.value = e;
  }
  get map() {
    return this.uniforms.map.value;
  }
  set map(e) {
    this.uniforms.map.value = e;
  }
  get roughnessMap() {
    return this.uniforms.roughnessMap.value;
  }
  set roughnessMap(e) {
    this.uniforms.roughnessMap.value = e;
  }
  get normalMap() {
    return this.uniforms.normalMap.value;
  }
  set normalMap(e) {
    this.uniforms.normalMap.value = e;
  }
  get aoMap() {
    return this.uniforms.aoMap.value;
  }
  set aoMap(e) {
    this.uniforms.aoMap.value = e;
  }
  get lightMap() {
    return this.uniforms.lightMap.value;
  }
  set lightMap(e) {
    this.uniforms.lightMap.value = e;
  }
  get lightMapIntensity() {
    return this.uniforms.lightMapIntensity.value;
  }
  set lightMapIntensity(e) {
    this.uniforms.lightMapIntensity.value = e;
  }
  get lightMap2() {
    return this.uniforms.lightMap2.value;
  }
  set lightMap2(e) {
    this.uniforms.lightMap2.value = e;
  }
  get lightMapBlending() {
    return this.uniforms.lightMapBlending.value;
  }
  set lightMapBlending(e) {
    this.uniforms.lightMapBlending.value = e;
  }
  get reflectIntensity() {
    return this.uniforms.reflectIntensity.value;
  }
  set reflectIntensity(e) {
    this.uniforms.reflectIntensity.value = e;
  }
}
Ce([S], ne.prototype, "color");
Ce([S({ min: 0, max: 1, step: 0.01 })], ne.prototype, "alpha");
Ce([S({ min: 0, max: 1, step: 0.01 })], ne.prototype, "metalness");
Ce([S({ min: 0, max: 1, step: 0.01 })], ne.prototype, "roughness");
Ce([S({ min: 0, max: 1, step: 0.01 })], ne.prototype, "lightMapBlending");
const me = class me extends ve {
  constructor() {
    super(...arguments);
    s(this, "_root");
    s(this, "_instanceCount", 20);
    s(this, "_instanceDistance", 20);
    s(this, "_instanceRoads", []);
    s(this, "_instanceBuildings", []);
    s(this, "_instanceFakeLight");
    s(this, "_maxDistance", (this._instanceCount / 2) * this._instanceDistance);
    s(this, "_uniforms", {
      uColor: { value: new P(0) },
      uLightColor: { value: new P(0) },
      uCityPercent: { value: 0 },
    });
  }
  onPre() {
    const e = this.viewer;
    return Promise.all([
      T(
        e,
        { url: "models/sm_scene01_building_dynamic.glb" },
        "scene01_building",
      ),
      T(e, { url: "models/sm_scene01_dynamic.glb" }, "scene01_dynamic"),
      T(e, { url: "models/sm_scene01_lights.glb" }, "scene01_lights"),
      T(
        e,
        {
          url: "textures/t_road_roughness.jpg",
          colorSpace: Te,
          wrapS: K,
          wrapT: K,
        },
        "t_road_roughness",
      ),
      T(
        e,
        { url: "textures/t_buildinglight.png", flipY: !1, wrapS: K, wrapT: K },
        "scene01_buildinglight",
      ),
      T(e, { url: "textures/lm_road.jpg", channel: 1, flipY: !1 }, "lm_road"),
      T(
        e,
        { url: "textures/t_road_bc.jpg", flipY: !1, wrapS: K, wrapT: K },
        "t_road",
      ),
      T(e, { url: "textures/t_hulan_bc.jpg", flipY: !1 }, "t_hulan"),
    ]);
  }
  onInit() {
    const { __position: e, __matrix: t } = me,
      o = new se();
    ((o.map = y("scene01_buildinglight")),
      (o.color = this._uniforms.uColor.value));
    const n = new ne();
    ((n.name = "mRoad"),
      (n.map = y("t_road")),
      (n.aoMap = y("ao_floor")),
      (n.lightMap = y("lm_road")),
      (n.normalMap = y("t_floor_normal")),
      (n.roughnessMap = y("t_road_roughness")),
      (n.lightMapIntensity = 4),
      (n.uniforms.uDistance = i.sharedUnifoms.uDistance),
      (n.uniforms.reflectMap = i.sharedUnifoms.reflectMap),
      (n.uniforms.textureMatrix = i.sharedUnifoms.textureMatrix),
      (n.uniforms.color = this._uniforms.uColor),
      (n.uniforms.uPercent = this._uniforms.uCityPercent));
    const l = new se();
    ((l.map = y("t_hulan")),
      (l.lightMap = y("lm_road")),
      (l.lightMapIntensity = 4),
      (l.color = this._uniforms.uColor.value));
    const m = new se();
    m.color = this._uniforms.uLightColor.value;
    const h = new se({ side: ft });
    h.color = this._uniforms.uLightColor.value;
    const d = this.viewer,
      C = y("scene01_dynamic");
    this._root = d.addNode(ue);
    const x = _.findChild(y("scene01_lights"), "Plane025"),
      L = (this._instanceFakeLight = new pe(x.geometry, h, 2));
    d.addNode(L, { parent: this._root, layer: F.ENV_REFLECT });
    for (let u = 0; u < 2; u++)
      (e.set(u * 20 + 10, 0, 0), t.setPosition(e), L.setMatrixAt(u, t));
    const I = _.findChild(C, "floor"),
      M = new pe(I.geometry, n, this._instanceCount);
    this._instanceRoads.push(d.addNode(M, { parent: this._root }));
    const A = _.findChild(C, "light"),
      D = new pe(A.geometry, m, this._instanceCount);
    this._instanceRoads.push(
      d.addNode(D, {
        parent: this._root,
        layer: F.DEFAULT | F.ENV_REFLECT | F.PLANE_REFLECT,
      }),
    );
    const W = _.findChild(C, "hulan"),
      f = new pe(W.geometry, l, this._instanceCount);
    this._instanceRoads.push(
      d.addNode(f, { parent: this._root, layer: F.DEFAULT | F.PLANE_REFLECT }),
    );
    for (let u = 0; u < this._instanceCount; u++)
      (e.set(
        (this._instanceCount / 2 - 0.5 - u) * this._instanceDistance,
        0,
        0,
      ),
        t.setPosition(e),
        this._instanceRoads.forEach((X) => X.setMatrixAt(u, t)));
    const B = _.findChild(y("scene01_building"), "building"),
      H = new pe(B.geometry, o, this._instanceCount * 2);
    this._instanceBuildings.push(
      d.addNode(H, { parent: this._root, layer: F.DEFAULT | F.ENV_REFLECT }),
    );
    for (let u = 0, X = this._instanceCount * 2; u < X; u++)
      (u < this._instanceCount
        ? ((e.x = (this._instanceCount / 2 - 0.5 - u) * this._instanceDistance),
          (e.y = -Math.random() * 30),
          (e.z = 25 + (Math.random() - 0.5) * 10))
        : ((e.x =
            this._instanceCount / 2 -
            0.5 -
            (u - this._instanceCount) * this._instanceDistance),
          (e.y = -Math.random() * 30),
          (e.z = -25 + (Math.random() - 0.5) * 10)),
        t.setPosition(e),
        this._instanceBuildings.forEach((at) => at.setMatrixAt(u, t)));
  }
  onEnter(e) {
    (super.onEnter(e), (this._root.visible = !0));
    const t = this.viewer;
    (t.killTweensOf(this._uniforms.uColor),
      t
        .timeline(this._uniforms.uColor)
        .to({ value: new P(16777215) }, 5, { easing: j.Cubic.InOut })
        .start(),
      t.killTweensOf(this._uniforms.uLightColor),
      t
        .timeline(this._uniforms.uLightColor)
        .to({ value: new P(16777215).multiplyScalar(2) }, 5, {
          easing: j.Cubic.InOut,
        })
        .start(),
      t.killTweensOf(i.motion),
      t
        .timeline(i.motion)
        .delay(1)
        .to({ rotationAmplitude: 0.2, positionAmplitude: 0.5 }, 4, {
          easing: j.Cubic.InOut,
        })
        .start(),
      t.killTweensOf(this._uniforms.uCityPercent),
      t
        .timeline(this._uniforms.uCityPercent)
        .to({ value: 1 }, 2, { easing: j.Cubic.InOut })
        .start(),
      (i.velocity = 16),
      (i.accelerate = 0.5),
      i.freeCamera.gotoPOI({ springLength: 8, fov: 60, smoothing: 1 }));
  }
  onExit() {
    const e = this.viewer;
    (e.killTweensOf(this._uniforms.uColor),
      e
        .timeline(this._uniforms.uColor)
        .to({ value: new P(0) }, 1)
        .call(() => (this._root.visible = !1))
        .start(),
      e.killTweensOf(this._uniforms.uLightColor),
      e
        .timeline(this._uniforms.uLightColor)
        .to({ value: new P(0) }, 1)
        .start(),
      e.killTweensOf(i.motion),
      e
        .timeline(i.motion)
        .to({ rotationAmplitude: 0, positionAmplitude: 0 }, 1)
        .start(),
      e.killTweensOf(this._uniforms.uCityPercent),
      e
        .timeline(this._uniforms.uCityPercent)
        .to({ value: 0 }, 2, { easing: j.Cubic.InOut })
        .start());
  }
  onUpdate(e) {
    if (i.movement <= 0.001) return;
    const t = i.movement;
    i.movement = 0;
    const { __matrix: o, __position: n } = me,
      l = o.elements;
    if (this._instanceFakeLight)
      for (let m = 0; m < 2; m++)
        (this._instanceFakeLight.getMatrixAt(m, o),
          n.set(l[12], l[13], l[14]),
          n.x + t > 20 ? (n.x += t - 40) : (n.x += t),
          o.setPosition(n),
          this._instanceFakeLight.setMatrixAt(m, o),
          (this._instanceFakeLight.instanceMatrix.needsUpdate = !0));
    for (let m = 0; m < this._instanceCount; m++)
      this._instanceRoads.forEach((h) => {
        (h.getMatrixAt(m, o),
          n.set(l[12], l[13], l[14]),
          n.x + t > this._maxDistance
            ? (n.x += t - 2 * this._maxDistance)
            : (n.x += t),
          o.setPosition(n),
          h.setMatrixAt(m, o),
          (h.instanceMatrix.needsUpdate = !0));
      });
    for (let m = 0, h = this._instanceCount * 2; m < h; m++)
      this._instanceBuildings.forEach((d) => {
        (d.getMatrixAt(m, o),
          n.set(l[12], l[13], l[14]),
          n.x + t > this._maxDistance
            ? ((n.x += t - 2 * this._maxDistance),
              (n.y = -Math.random() * 30),
              m < this._instanceCount
                ? (n.z = 25 + (Math.random() - 0.5) * 10)
                : (n.z = -25 + (Math.random() - 0.5) * 10))
            : (n.x += t),
          o.setPosition(n),
          d.setMatrixAt(m, o),
          (d.instanceMatrix.needsUpdate = !0));
      });
  }
};
(s(me, "__position", new p()), s(me, "__matrix", new He()));
let Re = me;
const $t = `
#include <noise>

varying vec2 vUv;
varying vec3 vPositionWS;

uniform sampler2D aoMap;
uniform sampler2D tCell;
uniform float     uPercent;

void main() {
    vec3 movePositionW = vPositionWS;
    float cellMask = texture(tCell,movePositionW.xz*6.).r;
    float noiseMask = ((noise2d(movePositionW.xz*0.2)+1.3)/2.);
    noiseMask = pow(noiseMask,3.);
    float dis = distance(vPositionWS,vec3(0.));
    float disMask = 1.-smoothstep(0.,25.,dis);
    disMask = pow(disMask,4.);

    float vStep = smoothstep(0.,-5.,dis - uPercent*15.);
    if(vStep*cellMask<0.05)discard;

    gl_FragColor = vec4(vec3(noiseMask*cellMask*disMask*vStep*0.8),1.);
    gl_FragColor.rgb *= texture2D(aoMap, vec2(vPositionWS.x, -vPositionWS.z) / 3.97104 * 0.5 + 0.5).r;
}
`;
class Kt extends ve {
  constructor() {
    super(...arguments);
    s(this, "_root");
    s(this, "_uniforms", { uNavMapPercent: { value: 0 } });
  }
  onPre() {
    const e = this.viewer;
    return Promise.all([
      T(e, { url: "models/sm_scene02.glb" }, "scene02"),
      T(e, { url: "textures/t_linearFiliter.png" }, "t_linearFiliter"),
      T(e, { url: "textures/t_compass.jpg" }, "t_compass"),
      T(
        e,
        { url: "textures/point.png", wrapS: K, wrapT: K, anisotropy: 8 },
        "t_point",
      ),
    ]);
  }
  onInit() {
    const e = new Z({
        name: "mSign",
        transparent: !0,
        depthWrite: !1,
        vertexShader: _e,
        fragmentShader: Rt,
        uniforms: {
          tAlpha: { value: y("t_compass") },
          uColor: { value: new P("#a9fecd") },
          uIntensity: { value: 1.4 },
          uPercent: this._uniforms.uNavMapPercent,
        },
      }),
      t = new Z({
        name: "m_driveLine",
        vertexShader: _e,
        fragmentShader: Fe,
        defines: { USE_MASK: !0, USE_AOMAP: !0 },
        uniforms: {
          aoMap: { value: y("ao_floor") },
          uColor: { value: new P(9737364) },
          uIntensity: { value: 1 },
          tMask: { value: y("t_linearFiliter") },
          uPercent: this._uniforms.uNavMapPercent,
        },
      }),
      o = this.viewer;
    this._root = o.addNode(ue);
    const n = y("scene02"),
      l = _.findChild(n, "Circle");
    l.material = e;
    const m = _.findChild(n, "BezierCurve001");
    m.material = t;
    const h = _.findChild(n, "BezierCurve");
    ((h.material = t), o.addNode(n, { parent: this._root }));
    const d = new Z({
        name: "M_scene02_floor",
        uniforms: {
          uPercent: this._uniforms.uNavMapPercent,
          tCell: { value: y("t_point") },
          aoMap: { value: y("ao_floor") },
        },
        vertexShader: _e,
        fragmentShader: $t,
      }),
      C = new Ge(new ut(50, 50), d);
    (C.rotateX(-Math.PI / 2), o.addNode(C));
  }
  onEnter(e) {
    (super.onEnter(e),
      (this._root.visible = !0),
      (i.velocity = 0),
      (i.freeCamera.phiMin = 0.5),
      (i.freeCamera.phiMax = Math.PI / 2 - 0.1),
      e.action === te.RESET
        ? i.freeCamera.gotoPOI({
            fov: 25,
            springLength: 16,
            smoothing: 3,
            lookAt: new p(0, 1, 0),
            theta: -1.58,
            phi: 1.41,
          })
        : i.freeCamera.gotoPOI({ fov: 25, springLength: 16, smoothing: 3 }));
    const t = this.viewer;
    (t.killTweensOf(this._uniforms.uNavMapPercent),
      t
        .timeline(this._uniforms.uNavMapPercent)
        .to({ value: 1 }, 2, { easing: j.Cubic.InOut })
        .start(),
      (i.sharedFloor.visible = !0),
      t.killTweensOf(i.sharedFloor.material),
      t
        .timeline(i.sharedFloor.material)
        .to({ color: new P(0), alpha: 0.2 }, 2)
        .start());
  }
  onExit() {
    const e = this.viewer;
    (e.killTweensOf(this._uniforms.uNavMapPercent),
      e
        .timeline(this._uniforms.uNavMapPercent)
        .to({ value: 0 }, 2, { easing: j.Cubic.InOut })
        .call(() => {
          this._root.visible = !1;
        })
        .start(),
      e.killTweensOf(i.sharedFloor.material),
      e
        .timeline(i.sharedFloor.material)
        .to({ color: new P(0), alpha: 0 }, 1)
        .call(() => {
          i.sharedFloor.visible = !1;
        })
        .start());
  }
}
const Zt = `
#include <noise>

attribute vec3 color;

varying vec3 vPosition;
varying vec3 vInstanceColor;

varying float npParam;
varying float waveParam;

varying float randomNum01;
varying float randomNum02;
varying float randomNum03;

uniform float uDistance;
uniform float uTime;
uniform float uPercent;
uniform float uVmin;

#define PI 3.1415926535

void main() {
    vInstanceColor = color;
    vPosition = position;
    vPosition.x = mod(vPosition.x + uDistance + 76.3, 152.6) - 76.3;

    //入场效果+流动效果
    vPosition = vPosition + (random21(position.xy + position.yz)-0.5) * (1. - uPercent) * 50.;
    // vPosition = vPosition + (cameraPosition+(random21(position.xy + position.yz)-0.5)*5. - vPosition) * (1. - uPercent); //* 50.;

    //流动参数
    float waveSize = 4.;
    float disToCenter = distance(vPosition, vec3(0.));
    float flow = mod(uTime, 2.5) * 40. - waveSize;
    waveParam = smoothstep(flow + waveSize, flow, disToCenter) * smoothstep(flow - waveSize, flow, disToCenter) * (1. - (flow + waveSize) / 100.);

    vec3 dirToCenter = normalize(vPosition - vec3(0.));
    vPosition += vec3(0., waveParam, 0.);

    //控制粒子停止移动
    waveSize = 20.;

    flow = mod(uTime, 2.5) * 40. - waveSize;
    float front = clamp(flow + waveSize, 0.002, 99999.);
    float back = clamp(flow - waveSize, 0., 99999.);
    float mid = clamp(flow, 0.001, 99999.);

    float kk = (1. - (flow + waveSize) / 100.);
    float ff = sqrt(smoothstep(front, mid, disToCenter));
    float bb = sqrt(smoothstep(back, mid, disToCenter));

    //更大的wave
    float np = ff * bb * kk;
    npParam = np;

    float randomNum = random21(position.xy + position.zz);
    vec3 randomVec = vec3(sin(uTime + randomNum * 23.324), cos(uTime + randomNum * 143.432), sin(uTime + randomNum * 576.423));
    vec3 moveAblePos = smoothstep(0.2 + 0.8 * np, 1., randomNum) * randomVec;
    // float wSize = SIZE - SIZE * (1. - np);
    float vMinSize = uVmin/9.5;
    float wSize = vMinSize - vMinSize * (1. - np) * 0.5;

    vPosition += moveAblePos * smoothstep(5., 50., disToCenter) * (1. - np) * 5.;

    randomNum01 = random21(vPosition.zz + vPosition.yx);
    randomNum02 = random21(vPosition.xx + vPosition.zy);
    randomNum03 = noise2d(vPosition.zy + vec2(0., vPosition.x * 0.02 + uTime));

    vec4 mvPosition = modelViewMatrix * vec4(vPosition, 1.0);
    gl_Position = projectionMatrix * mvPosition;
    gl_PointSize = clamp(wSize * (1. / - mvPosition.z),0.,vMinSize/10.);
}`,
  Qt = `
#include <noise>

varying vec3 vPosition;
varying vec3 vInstanceColor;
varying vec2 vUv;
varying float waveParam;
varying float npParam;

varying float randomNum01;
varying float randomNum02;
varying float randomNum03;

uniform sampler2D aoMap;
uniform vec3 uWaveColor;
uniform float uWaveIntensity;
uniform float uColorIntensity;
uniform float uPercent;
uniform float uTime;

#define PI 3.1415926535

void main() {
    float dis = distance(gl_PointCoord, vec2(0.5));
    float op = uPercent * 0.5;
    if (dis > op) discard;

    float disToCamera = distance(vPosition, cameraPosition.xyz);
    float disToCenterX = abs(vPosition.x);
    if (randomNum01 > smoothstep(75., 10., disToCenterX)) discard;

    vec3 color = mix(vInstanceColor * uColorIntensity * (1. + 2. * pow(randomNum02, 5.) + 300. * pow(clamp(randomNum03, 0., 1.), 8.)), uWaveColor * uWaveIntensity, vec3(waveParam));

    gl_FragColor = vec4(vec3(mix(vec3(0.3, 0.5, 0.6) * (1. + 100. * pow(clamp(randomNum03, 0., 1.), 8.)), color, vec3(npParam))), 1.);
    gl_FragColor.rgb *= texture2D(aoMap, vec2(vPosition.x, -vPosition.z) / 3.97104 * 0.5 + 0.5).r;
}
`,
  de = class de extends ve {
    constructor() {
      super(...arguments);
      s(this, "_root");
      s(this, "_uniforms", { uPointCloudPercent: { value: 0 } });
    }
    async onPre() {
      const e = this.viewer;
      (await Promise.all([
        T(e, { url: "models/sm_scene02_dynamic.glb" }, "scene02_dynamic"),
      ]),
        (this._root = e.addNode(ue, { visible: !1 })),
        this._root.add(this._preparePointCloud()));
    }
    _preparePointCloud() {
      const e = new Z({
          vertexShader: Zt,
          fragmentShader: Qt,
          uniforms: {
            aoMap: { value: y("ao_floor") },
            uWaveIntensity: { value: 3 },
            uWaveColor: { value: new P("#6cbffe") },
            uColorIntensity: { value: 1 },
            uPercent: this._uniforms.uPointCloudPercent,
            uDistance: i.sharedUnifoms.uDistance,
            uTime: i.sharedUnifoms.uTime,
            uVmin: i.sharedUnifoms.uVmin,
          },
        }),
        t = y("scene02_dynamic"),
        o = _.findChild(t, "Plane002");
      let n = new tt(o).setWeightAttribute(null).build();
      const l = 6e5,
        m = new Be(),
        h = new ArrayBuffer(l * 16),
        d = new Float32Array(h),
        C = new Uint8Array(h),
        { __position: x, __normal: L, __color: I } = de,
        M = we.compress ? 255 / 65535 : 255;
      for (let f = 0; f < d.length; f += 4) {
        (n.sample(x, L, I),
          (d[f + 0] = x.x),
          (d[f + 1] = x.y),
          (d[f + 2] = x.z));
        const B = (f + 3) * 4;
        ((C[B + 0] = I.r * M),
          (C[B + 1] = I.g * M),
          (C[B + 2] = I.b * M),
          (C[B + 3] = 0));
      }
      const A = new Le(d, 4),
        D = new Le(C, 16);
      (m.setAttribute("position", new Ee(A, 3, 0, !1)),
        m.setAttribute("color", new Ee(D, 3, 12, !0)));
      const W = new je(m, e);
      return ((W.layers.mask = F.DEFAULT | F.ENV_REFLECT), W);
    }
    onInit() {}
    onEnter(e) {
      (super.onEnter(e),
        (this._root.visible = !0),
        i.freeCamera.gotoPOI({ springLength: 60, smoothing: 1 }),
        (i.velocity = 16));
      const t = this.viewer;
      (t.killTweensOf(this._uniforms.uPointCloudPercent),
        t
          .timeline(this._uniforms.uPointCloudPercent)
          .to({ value: 1 }, 2, { easing: j.Cubic.InOut })
          .start());
    }
    onExit() {
      const e = this.viewer;
      (e.killTweensOf(this._uniforms.uPointCloudPercent),
        e
          .timeline(this._uniforms.uPointCloudPercent)
          .to({ value: 0 }, 2, { easing: j.Cubic.InOut })
          .call(() => {
            this._root.visible = !1;
          })
          .start());
    }
  };
(s(de, "__position", new p()),
  s(de, "__normal", new p()),
  s(de, "__color", new P()));
let We = de;
const Jt = `
#include <noise>
varying vec3 vPosition;
varying float vWave;
uniform float uTime;
uniform float uPercent;
uniform float uVmin;

varying float vParamsCIndex;
attribute float paramsCIndex;
attribute float paramsCDegree;

void main() {
    vec3 transformed = position;

    float loopY = -(abs(paramsCDegree-0.5)-0.5)*2.;

    float noiseMask = (noise2d(vec2(loopY*5.,uTime*0.5))+1.)/2.;

    // noiseMask*=(noise2d(vec2(loopY*2.,uTime*0.5))+1.)/2.;
    noiseMask = pow(noiseMask,3.);

    transformed.y+=noiseMask*(paramsCIndex/2.);

    //消散效果
    float waveSize = 0.5;
    float front = uPercent*(1.+waveSize)-waveSize;
    float back = uPercent*(1.+waveSize);
    float waveStep = smoothstep(front,back,paramsCDegree);

    float noiseWave = random21(position.xz);
    noiseWave = clamp((waveStep*2.-1.)+noiseWave,0.,1.);
    vWave = noiseWave;
    transformed.y+=noiseWave*4.;

    vPosition = transformed;
    vec4 mvPosition = modelViewMatrix * vec4(transformed, 1.0);

    float vMinSize = uVmin/30.;
    float wSize = vMinSize;
    vParamsCIndex=paramsCIndex;
    gl_Position = projectionMatrix * mvPosition;
    gl_PointSize = wSize * (1. / - mvPosition.z);//clamp(wSize * (1. / - mvPosition.z),0.,vMinSize/10.);
}`,
  ei = `
varying vec2      vUv;
varying vec3      vPosition;
varying float vWave;
varying float randomNum01;
varying float vParamsCIndex;

uniform float     uPercent;
uniform vec3 uColor;
uniform float uColorIntensity; 
void main() {
    float dis = distance(gl_PointCoord, vec2(0.5));
    // float op = uPercent * 0.5;
    float op = 0.5;
    if (dis > op) discard;
    if(vWave>0.99) discard;

    gl_FragColor = vec4(uColor*uColorIntensity*(1.+vParamsCIndex/2.), 1.);

    #include <tonemapping_fragment>
    #include <colorspace_fragment>
}`,
  w = {
    Loading: 0,
    BeginAnim: 1,
    State1: 2,
    State2: 3,
    State3: 4,
    State4: 5,
    State5: 6,
  },
  b = {
    PRELOADED: "prelaoded",
    PRESSED: "pressed",
    STATE_CHANGED: "statechanged",
    COLORBODY_CHANGED: "colorbodychanged",
    COLORINTERIOR_CHANGED: "colorinteriorchanged",
  };
class ti extends ht {
  constructor() {
    super(...arguments);
    s(this, "_state", w.Loading);
    s(this, "_pressed", !1);
    s(this, "_preloaded", !1);
    s(this, "_colorBodyIndex", 0);
    s(this, "_colorInteriorIndex", 0);
    s(this, "_flag", !0);
  }
  get preloaded() {
    return this._preloaded;
  }
  set preloaded(e) {
    ((this._preloaded = e), this.emit(b.PRELOADED, e));
  }
  get state() {
    return this._state;
  }
  set state(e) {
    this._flag &&
      ((this._flag = !1),
      setTimeout(() => {
        this._flag = !0;
      }, 300),
      (this._state = e),
      this.emit(b.STATE_CHANGED, e));
  }
  get presssed() {
    return this._pressed;
  }
  set presssed(e) {
    ((this._pressed = e), this.emit(b.PRESSED, e));
  }
  get colorBodyIndex() {
    return this._colorBodyIndex;
  }
  set colorBodyIndex(e) {
    ((this._colorBodyIndex = e), this.emit(b.COLORBODY_CHANGED, e));
  }
  get colorInteriorIndex() {
    return this._colorInteriorIndex;
  }
  set colorInteriorIndex(e) {
    ((this._colorInteriorIndex = e), this.emit(b.COLORINTERIOR_CHANGED, e));
  }
}
const v = window.v = new ti(),
  ii = new P(16777215).convertLinearToSRGB();
class oi extends ve {
  constructor() {
    super(...arguments);
    s(this, "_root");
    s(this, "_uniforms", {
      uPercent: { value: 0 },
      uVmin: { value: 1e3 },
      uColor: { value: new P("#b2eaff") },
    });
    s(this, "_HSL", { h: 0, s: 0, l: 0 });
    s(this, "_mixD", 0);
    s(this, "_elapsed", 0);
  }
  onPre() {}
  onInit() {
    const e = this.viewer;
    ((this._root = e.addNode(ue, { name: "HomeScene" })),
      (this._root.visible = !1));
    const t = this._createMusicParticle();
    (e.addNode(t, {
      parent: this._root,
      layer: F.DEFAULT | F.ENV_REFLECT | F.PLANE_REFLECT,
    }),
      (this._uniforms.uVmin.value = Math.min(e.height, e.width)),
      e.on(
        G.RESIZE,
        (o, n) => {
          this._uniforms.uVmin.value = Math.min(o, n);
        },
        this,
      ));
  }
  onEnter(e) {
    super.onEnter(e);
    const t = this.viewer;
    ((i.sharedFloor.visible = !0),
      (this._root.visible = !0),
      t.killTweensOf(i.c16Uniforms.uCarLightIntensity),
      (i.c16Uniforms.uCarLightIntensity.value = 5),
      t.killTweensOf(i.c16Uniforms.uInsideLightMapIntensity),
      t
        .timeline(i.c16Uniforms.uInsideLightMapIntensity)
        .to({ value: 3 }, 1)
        .start(),
      t.killTweensOf(i.sharedFloor.material),
      t
        .timeline(i.sharedFloor.material)
        .to({ color: ii.clone(), alpha: 0.32, lightMapBlending: 1 }, 2, {
          easing: j.Cubic.InOut,
        })
        .start());
    const o = y("c16"),
      n = _.getMaterial(o, "m_glass"),
      l = _.getMaterial(o, "m_black");
    (t.killTweensOf(l),
      t
        .timeline(l)
        .to({ emissive: new P(3158064).convertLinearToSRGB() }, 2)
        .start(),
      t.killTweensOf(n),
      t.timeline(n).to({ opacity: 0 }, 2).start(),
      e.action === te.RESET
        ? i.freeCamera.gotoPOI({
            springLength: 10,
            theta: -0.84,
            phi: 1.47,
            fov: 33.4,
            lookAt: new p(0, 1, 0),
            smoothing: 3,
          })
        : i.freeCamera.gotoPOI({ springLength: 10, fov: 33.4, smoothing: 3 }),
      t.killTweensOf(this._uniforms.uPercent),
      t
        .timeline(this._uniforms.uPercent)
        .delay(0.2)
        .to({ value: 1 }, 3, { easing: j.Cubic.Out })
        .start(),
      this.viewer.killTweensOf(i.bloom),
      this.viewer.timeline(i.bloom).to({ luminanceThreshold: 0 }, 1).start());
  }
  onExit() {
    const e = this.viewer,
      t = y("c16"),
      o = _.getMaterial(t, "m_glass"),
      n = _.getMaterial(t, "m_black");
    (e.killTweensOf(i.c16Uniforms.uCarLightIntensity),
      e
        .timeline(i.c16Uniforms.uCarLightIntensity)
        .delay(2)
        .call(() => (i.c16Uniforms.uCarLightIntensity.value = 20))
        .start(),
      e.killTweensOf(i.c16Uniforms.uInsideLightMapIntensity),
      e
        .timeline(i.c16Uniforms.uInsideLightMapIntensity)
        .to({ value: 1 }, 2)
        .start(),
      e.killTweensOf(n),
      e
        .timeline(n)
        .to({ emissive: new P(0) }, 2)
        .start(),
      e.killTweensOf(i.sharedFloor.material),
      e
        .timeline(i.sharedFloor.material)
        .to({ color: new P(0), alpha: 0 }, 2)
        .call(() => {
          i.sharedFloor.visible = !1;
        })
        .start(),
      e.killTweensOf(o),
      e.timeline(o).to({ opacity: 0.9 }, 2).start(),
      e.killTweensOf(this._uniforms.uPercent),
      e
        .timeline(this._uniforms.uPercent)
        .to({ value: 0 }, 1.5)
        .call(() => {
          this._root.visible = !1;
        })
        .start(),
      e.killTweensOf(i.bloom),
      e.timeline(i.bloom).to({ luminanceThreshold: 0.5 }, 1).start());
  }
  onUpdate(e) {
    const t = this._uniforms.uColor.value;
    (t.getHSL(this._HSL), (this._elapsed += e * 0.2));
    let o = this._elapsed % 2;
    ((o = Math.abs(o - 1)),
      (this._HSL.h = 0.3 + 0.3 * o),
      t.setHSL(this._HSL.h, this._HSL.s, this._HSL.l),
      v.state === w.State3 && v.presssed
        ? this._mixD + e <= 1
          ? (this._mixD += e)
          : (this._mixD = 1)
        : this._mixD - e >= 0
          ? (this._mixD -= e)
          : (this._mixD = 0));
    const n = i.sharedUnifoms.uInteriorLightColor.value;
    (n.set("#ffffff"), n.lerp(t, this._mixD));
  }
  _createMusicParticle() {
    const m = new Float32Array(7500),
      h = new Uint8Array(2500),
      d = new Float32Array(2500);
    for (let A = 0; A < 5; A++)
      for (let D = 0; D < 500; D++) {
        const W = 4 + A * 0.06,
          f = Math.PI * 2 * (D / 500),
          B = A * 500 + D;
        ((m[B * 3] = Math.cos(f) * W),
          (m[B * 3 + 1] = 0.1),
          (m[B * 3 + 2] = Math.sin(f) * W),
          (h[B] = A),
          (d[B] = f / (Math.PI * 2)));
      }
    const C = new ee(m, 3),
      x = new ee(h, 1),
      L = new ee(d, 1),
      I = new Be();
    (I.setAttribute("position", C),
      I.setAttribute("paramsCIndex", x),
      I.setAttribute("paramsCDegree", L));
    const M = new Z({
      name: "mMusicParticles",
      vertexShader: Jt,
      fragmentShader: ei,
      uniforms: {
        uPercent: this._uniforms.uPercent,
        uVmin: this._uniforms.uVmin,
        uTime: i.sharedUnifoms.uTime,
        uColor: this._uniforms.uColor,
        uColorIntensity: { value: 1 },
      },
    });
    return new je(I, M);
  }
}
const ni = `
#include <noise>

attribute vec3 color;

varying vec3 vPosition;
varying vec3 vInstanceColor;
varying vec3 vNormalW;

varying float vFresnel;
varying float randomNum04;

uniform float uTime;
uniform float uPercent;
uniform float uVmin;

#define PI 3.1415926535

void main() {
    vInstanceColor = color;
    vPosition = position;

    float randomNum01 = random21(vPosition.zz + vPosition.yx)-0.5;
    float randomNum02 = random21(vPosition.xx + vPosition.zy)-0.5;
    float randomNum03 = random21(vPosition.yy + vPosition.xz)-0.5;

    randomNum04 = random21(vPosition.zz + vPosition.xy)-0.5;
    //入场效果+流动效果
    vPosition = vPosition + vec3(randomNum01,randomNum02,randomNum03)*(1.-uPercent)*0.4;
    vec4 mvPosition = modelViewMatrix * vec4(vPosition, 1.0);

    float vMinSize = uVmin/100.;
    float wSize = vMinSize;

    vNormalW = normal;
    vec3 viewDirectionW = normalize(cameraPosition - position);
	vFresnel = dot(viewDirectionW, vNormalW);

    gl_Position = projectionMatrix * mvPosition;
    gl_PointSize = clamp(wSize * (1. / - mvPosition.z),0.,vMinSize/10.);
}`,
  ai = `
#include <noise>

varying vec3 vPosition;
varying vec3 vInstanceColor;
varying vec2 vUv;
varying vec3 vNormalW;

varying float vFresnel;
varying float randomNum04;

uniform vec3 uColor0;
uniform float uColor0Intensity;
uniform vec3 uColor1;
uniform float uColor1Intensity;
uniform float uPercent;
uniform float uTime;

#define PI 3.1415926535

void main() {
    float dis = distance(gl_PointCoord, vec2(0.5));
    // float op = uPercent * 0.5;
    float op = 0.5;
    if (dis > op) discard;
    // if (dis > op) discard;
    if (uPercent < randomNum04+0.6) discard;

    vec3 color0 = uColor0*uColor0Intensity;
    vec3 color1 = uColor1*uColor1Intensity;

    vec3 color = mix(color0,color1,vec3(sqrt(abs(vFresnel))));

    gl_FragColor = vec4(color,1.);
}
`,
  fe = class fe extends ve {
    constructor() {
      super(...arguments);
      s(this, "_uniforms", { uPointCloudPercent: { value: 0 } });
      s(this, "_root");
    }
    async onPre() {
      const e = this.viewer;
      (await Promise.all([
        T(e, { url: "models/sm_scene03_props.glb" }, "scene03_people"),
      ]),
        (this._root = e.addNode(ue, { visible: !1 })),
        this._root.add(this._preparePointCloud()));
    }
    _preparePointCloud() {
      const e = new Z({
          name: "mPointCloudPeople",
          vertexShader: ni,
          fragmentShader: ai,
          uniforms: {
            uColor0: { value: new P("#ffffff") },
            uColor0Intensity: { value: 5 },
            uColor1: { value: new P("#1eff99") },
            uColor1Intensity: { value: 2 },
            uPercent: this._uniforms.uPointCloudPercent,
            uTime: i.sharedUnifoms.uTime,
            uVmin: i.sharedUnifoms.uVmin,
          },
        }),
        t = y("scene03_people"),
        o = _.findChild(t, "props005");
      let n = new tt(o).setWeightAttribute(null).build();
      const l = 2e4,
        m = new Be(),
        h = new ArrayBuffer(l * 16),
        d = new Float32Array(h),
        C = new Float32Array(l * 3),
        x = new Uint8Array(h),
        { __position: L, __normal: I, __color: M } = fe;
      for (let f = 0; f < d.length; f += 4) {
        (n.sample(L, I, M),
          (d[f + 0] = L.x),
          (d[f + 1] = L.y),
          (d[f + 2] = L.z),
          (C[(f / 4) * 3 + 0] = I.x),
          (C[(f / 4) * 3 + 1] = I.y),
          (C[(f / 4) * 3 + 2] = I.z));
        const B = (f + 3) * 4;
        ((x[B + 0] = M.r * 255),
          (x[B + 1] = M.g * 255),
          (x[B + 2] = M.b * 255),
          (x[B + 3] = 0));
      }
      const A = new Le(d, 4),
        D = new ee(C, 3),
        W = new Le(x, 16);
      return (
        m.setAttribute("position", new Ee(A, 3, 0, !1)),
        m.setAttribute("normal", D),
        m.setAttribute("color", new Ee(W, 3, 12, !0)),
        new je(m, e)
      );
    }
    onInit() {}
    onEnter(e) {
      super.onEnter(e);
      const t = this.viewer;
      ((this._root.visible = !0),
        (i.c16Uniforms.uDissolveBox.value = 1),
        t.killTweensOf(i.c16Uniforms.uCarLightIntensity),
        (i.c16Uniforms.uCarLightIntensity.value = 5),
        t.killTweensOf(i.c16Uniforms.uInsideLightMapIntensity),
        t
          .timeline(i.c16Uniforms.uInsideLightMapIntensity)
          .to({ value: 3 }, 1)
          .start(),
        (i.freeCamera.phiMin = 0.001),
        (i.freeCamera.phiMax = Math.PI / 2 - 0.1),
        e.action === te.RESET
          ? i.freeCamera.gotoPOI({
              springLength: 7.5,
              theta: 0,
              phi: Math.PI / 2 - 0.9,
              fov: 33.4,
              lookAt: new p(0, 1, 0),
              smoothing: 3,
            })
          : i.freeCamera.gotoPOI({
              springLength: 7.5,
              fov: 33.4,
              smoothing: 3,
            }),
        (i.sharedFloor.visible = !0),
        t.killTweensOf(i.sharedFloor.material),
        t
          .timeline(i.sharedFloor.material)
          .to({ color: new P(16777215), alpha: 1, lightMapBlending: 1 }, 2)
          .start(),
        t.killTweensOf(i.c16Uniforms.uDissolvePercent),
        t.timeline(i.c16Uniforms.uDissolvePercent).to({ value: 0 }, 2).start(),
        t.killTweensOf(this._uniforms.uPointCloudPercent),
        t
          .timeline(this._uniforms.uPointCloudPercent)
          .delay(0.5)
          .to({ value: 1 }, 1)
          .start());
    }
    onExit() {
      const e = this.viewer;
      (e.killTweensOf(i.c16Uniforms.uCarLightIntensity),
        e
          .timeline(i.c16Uniforms.uCarLightIntensity)
          .delay(2)
          .call(() => (i.c16Uniforms.uCarLightIntensity.value = 20))
          .start(),
        e.killTweensOf(i.c16Uniforms.uInsideLightMapIntensity),
        e
          .timeline(i.c16Uniforms.uInsideLightMapIntensity)
          .to({ value: 1 }, 2)
          .start(),
        e.killTweensOf(i.sharedFloor.material),
        e
          .timeline(i.sharedFloor.material)
          .to({ color: new P(0), alpha: 0 }, 2)
          .call(() => (i.sharedFloor.visible = !1))
          .start(),
        e.killTweensOf(i.c16Uniforms.uDissolvePercent),
        e
          .timeline(i.c16Uniforms.uDissolvePercent)
          .to({ value: 1 }, 2)
          .call(() => {
            i.c16Uniforms.uDissolveBox.value = 0;
          })
          .start(),
        e.killTweensOf(this._uniforms.uPointCloudPercent),
        e
          .timeline(this._uniforms.uPointCloudPercent)
          .delay(0.2)
          .to({ value: 0 }, 1)
          .delay(1)
          .call(() => {
            this._root.visible = !1;
          })
          .start());
    }
  };
(s(fe, "__position", new p()),
  s(fe, "__normal", new p()),
  s(fe, "__color", new P()));
let Ve = fe;
class ri extends Ae {
  onEnable() {
    (this.viewer.on(G.POINTER_DOWN, this._onPointerDown, this),
      this.viewer.on(G.POINTER_UP, this._onPointerUp, this),
      v.on(b.STATE_CHANGED, this._onStateChanged, this),
      v.on(b.PRESSED, this._onPressed, this),
      v.on(b.COLORBODY_CHANGED, this._onColorBodyChanged, this),
      v.on(b.COLORINTERIOR_CHANGED, this._onColorInteriorChanged, this));
  }
  onDisable() {
    (this.viewer.killTweensOf(this), v.targetOff(this));
  }
  start() {
    ((v.preloaded = !0), (v.state = w.BeginAnim));
  }
  _onPointerDown() {
    v.presssed = !0;
  }
  _onPointerUp() {
    v.presssed = !1;
  }
  _onStateChanged(r) {
    switch (r) {
      case w.BeginAnim:
        (N.runScene("HomeScene", { action: te.BEGIN }),
          this.schedule(() => (v.state = w.State1), 4, 1));
        break;
      case w.State1:
        N.runScene("HomeScene", { action: te.RESET });
        break;
      case w.State2:
        N.runScene("AIDriveScene", { action: te.RESET });
        break;
      case w.State3:
        N.runScene("InteriorDetail", { action: te.RESET });
        break;
    }
  }
  _onPressed(r) {
    switch (v.state) {
      case w.State1:
        r ? N.runScene("HomeCity") : N.runScene("HomeScene");
        break;
      case w.State2:
        r ? N.runScene("AIDriveRadar") : N.runScene("AIDriveScene");
        break;
      case w.State3:
        r ? N.runScene("InteriorScene") : N.runScene("InteriorDetail");
        break;
    }
  }
  _onColorBodyChanged(r) {
    switch (r) {
      case 0:
        (i.bodyColor.set("#dbdbdb"),
          (i.roughness = 0.07),
          (i.metalness = 0.04),
          i.normalScale.set(0, 0));
        break;
      case 1:
        (i.bodyColor.set("#c3d3d5"),
          (i.roughness = 0.14),
          (i.metalness = 0.1),
          i.normalScale.set(0, 0));
        break;
      case 2:
        (i.bodyColor.set("#2e2e2e"),
          (i.roughness = 0.23),
          (i.metalness = 0.81),
          i.normalScale.set(0.04, -0.04));
        break;
      case 3:
        (i.bodyColor.set("#0a0a0a"),
          (i.roughness = 0.23),
          (i.metalness = 0.81),
          i.normalScale.set(0.04, -0.04));
        break;
      case 4:
        (i.bodyColor.set("#002411"),
          (i.roughness = 0.23),
          (i.metalness = 0.81),
          i.normalScale.set(0.04, -0.04));
        break;
      case 5:
        (i.bodyColor.set("#1d0f29"),
          (i.roughness = 0.23),
          (i.metalness = 0.88),
          i.normalScale.set(0.04, -0.04));
        break;
    }
    i.bodyColor.convertLinearToSRGB();
    const e = y("c16"),
      t = _.getMaterial(e, "m_c16_body");
    (this.viewer.killTweensOf(t),
      this.viewer
        .timeline(t)
        .to(
          {
            color: i.bodyColor,
            metalness: i.metalness,
            roughness: i.roughness,
            normalScale: i.normalScale,
          },
          0.3,
        )
        .start());
  }
  _onColorInteriorChanged(r) {
    switch (r) {
      case 0:
        (i.interiorColor.set("#ffd6b3"), i.interiorFrameColor.set("#0d0d0d"));
        break;
      case 1:
        (i.interiorColor.set("#2e2636"), i.interiorFrameColor.set("#0d0d0d"));
        break;
      case 2:
        (i.interiorColor.set("#dc441e"), i.interiorFrameColor.set("#0d0d0d"));
        break;
    }
    const e = y("c16"),
      t = _.getMaterial(e, "mInside");
    (this.viewer.killTweensOf(t),
      this.viewer
        .timeline(t)
        .to({ color: i.interiorColor.clone().convertLinearToSRGB() }, 0.3)
        .start());
    const o = _.getMaterial(e, "mInsideFrame");
    (this.viewer.killTweensOf(o),
      this.viewer
        .timeline(o)
        .to({ color: i.interiorFrameColor.clone().convertLinearToSRGB() }, 0.3)
        .start());
  }
}
var si = Object.defineProperty,
  li = Object.getOwnPropertyDescriptor,
  z = (c, r, e, t) => {
    for (
      var o = t > 1 ? void 0 : t ? li(r, e) : r, n = c.length - 1, l;
      n >= 0;
      n--
    )
      (l = c[n]) && (o = (t ? l(r, e, o) : l(o)) || o);
    return (t && o && si(r, e, o), o);
  };
const { clamp: Ue, degToRad: ci } = De,
  { abs: mi, tan: di, PI: fi } = Math,
  Ie = fi * 2,
  ke = 0.001;
var R;
const O =
  ((R = class extends vt {
    constructor() {
      super(...arguments);
      s(this, "_button", -1);
      s(this, "_touchID", -1);
      s(this, "_preLoc0", new $());
      s(this, "_preLoc1", new $());
      s(this, "_spherical", new gt(1, Math.PI / 2));
      s(this, "_lookAt", new p());
      s(this, "_tempSmoothing", 6);
      s(this, "_tempRotateSmoothing", 6);
      s(this, "_targetTheta", 0);
      s(this, "_targetPhi", 0);
      s(this, "_targetSpringLength", 1);
      s(this, "_targetFov", this.fov);
      s(this, "_targetLookAt", new p());
      s(this, "forbidX", !1);
      s(this, "forbidY", !1);
      s(this, "forbidZ", !1);
      s(this, "forbidPanX", !0);
      s(this, "forbidPanY", !0);
      s(this, "forbitPanOffsetY", !1);
      s(this, "panSpeed", 1);
      s(this, "rotateSpeed", 1);
      s(this, "smoothing", 5);
      s(this, "rotateSmoothing", 5);
      s(this, "phiMin", ke);
      s(this, "phiMax", Math.PI - ke);
      s(this, "thetaMin", -1 / 0);
      s(this, "thetaMax", 1 / 0);
      s(this, "distanceMin", ke);
      s(this, "distanceMax", 1 / 0);
    }
    get lookAt() {
      return this._lookAt;
    }
    set lookAt(e) {
      (this._lookAt.copy(e), this._targetLookAt.copy(e));
    }
    get springLength() {
      return this._spherical.radius;
    }
    set springLength(e) {
      this._spherical.radius = this._targetSpringLength = e;
    }
    get theta() {
      return this._spherical.theta;
    }
    set theta(e) {
      this._spherical.theta = this._targetTheta = e;
    }
    get phi() {
      return this._spherical.phi;
    }
    set phi(e) {
      this._spherical.phi = this._targetPhi = e;
    }
    get fov() {
      return this.lens.fov;
    }
    set fov(e) {
      this.lens.fov = this._targetFov = e;
    }
    onEnable() {
      (this.viewer.on(G.POINTER_DOWN, this._onPointerDown, this),
        this.viewer.on(G.POINTER_UP, this._onPointerUp, this),
        this.viewer.on(G.POINTER_MOVE, this._onPointerMove, this),
        this.viewer.on(G.MOUSE_WHEEL, this._onMouseWheel, this),
        this.viewer.on(G.TOUCH_START, this._onTouchStart, this),
        this.viewer.on(G.TOUCH_MOVE, this._onTouchMove, this),
        this.reset());
    }
    onDisable() {
      (this.viewer.off(G.POINTER_DOWN, this._onPointerDown, this),
        this.viewer.off(G.POINTER_UP, this._onPointerUp, this),
        this.viewer.off(G.POINTER_MOVE, this._onPointerMove, this),
        this.viewer.off(G.MOUSE_WHEEL, this._onMouseWheel, this),
        this.viewer.off(G.TOUCH_START, this._onTouchStart, this),
        this.viewer.off(G.TOUCH_MOVE, this._onTouchMove, this));
    }
    reset() {
      ((this._button = -1),
        (this._touchID = -1),
        (this._targetTheta = this.theta),
        (this._targetPhi = this.phi),
        (this._targetSpringLength = this.springLength),
        this._targetLookAt.copy(this._lookAt),
        (this._targetFov = this.lens.fov));
    }
    _onPointerDown(e) {
      oe.isMobile ||
        ((this._button = e.button), this._preLoc0.set(e.pageX, e.pageY));
    }
    _onPointerUp(e) {
      oe.isMobile || (this._button = -1);
    }
    _onPointerMove(e) {
      if (oe.isMobile) return;
      const { __loc0: t, __panDelta: o, __rotateDelta: n } = R;
      switch ((t.set(e.pageX, e.pageY), this._button)) {
        case 0:
          (this._calculateRotatelDelta(n, this._preLoc0, t),
            this._calculateTargetSpringArm(n));
          break;
        case 1:
        case 2:
          (this._calculatePanDelta(o, this._preLoc0, t),
            this._calculateTargetLookAt(o));
          break;
      }
      this._preLoc0.copy(t);
    }
    _onMouseWheel(e) {
      e.deltaY > 0
        ? (this._targetSpringLength *= this._calculateDistanceScale(1 / 0.85))
        : e.deltaY < 0 &&
          (this._targetSpringLength *= this._calculateDistanceScale(0.85));
    }
    _onTouchStart(e) {
      if (!oe.isMobile) return;
      let t = e.touches;
      t.length > 1
        ? (this._preLoc0.set(t[0].pageX, t[0].pageY),
          this._preLoc1.set(t[1].pageX, t[1].pageY))
        : t.length > 0 &&
          ((this._touchID = t[0].identifier),
          this._preLoc0.set(t[0].pageX, t[0].pageY));
    }
    _onTouchMove(e) {
      if (!oe.isMobile) return;
      const {
        __loc0: t,
        __loc1: o,
        __panDelta: n,
        __rotateDelta: l,
        __preCenter: m,
        __center: h,
      } = R;
      let d = e.touches;
      d.length > 1
        ? (t.set(d[0].pageX, d[0].pageY),
          o.set(d[1].pageX, d[1].pageY),
          this.lookAt &&
            (this._targetSpringLength *= this._calculateDistanceScale(
              this._preLoc0.distanceTo(this._preLoc1) / t.distanceTo(o),
            )),
          m.copy(this._preLoc0).add(this._preLoc1).multiplyScalar(0.5),
          h.copy(t).add(o).multiplyScalar(0.5),
          this._calculatePanDelta(n, m, h),
          this._calculateTargetLookAt(n),
          this._preLoc0.copy(t),
          this._preLoc1.copy(o))
        : d.length > 0 &&
          this._touchID === d[0].identifier &&
          (t.set(d[0].pageX, d[0].pageY),
          this._calculateRotatelDelta(l, this._preLoc0, t),
          this._calculateTargetSpringArm(l),
          this._preLoc0.copy(t));
    }
    _calculateDistanceScale(e) {
      return (
        (this._tempRotateSmoothing = this.rotateSmoothing),
        this.forbidZ && (e = 1),
        e
      );
    }
    _calculateRotatelDelta(e, t, o) {
      this._tempRotateSmoothing = this.rotateSmoothing;
      const n = this.viewer.canvas;
      return (
        e
          .copy(o)
          .sub(t)
          .multiplyScalar((this.rotateSpeed * 2 * Math.PI) / n.height),
        (e.y = -e.y),
        this.forbidX && (e.x = 0),
        this.forbidY && (e.y = 0),
        e
      );
    }
    _calculatePanDelta(e, t, o) {
      this._tempRotateSmoothing = this.rotateSmoothing;
      const n = this.viewer.canvas;
      return (
        e
          .copy(o)
          .sub(t)
          .multiplyScalar(this.panSpeed / n.height),
        this.forbidPanX && (e.x = 0),
        this.forbidPanY && (e.y = 0),
        e
      );
    }
    _calculateTargetLookAt(e) {
      const { __xAxis: t, __yAxis: o, __posDelta: n } = R;
      (t.setFromMatrixColumn(this.node.matrix, 0),
        o.setFromMatrixColumn(this.node.matrix, 1),
        this.forbitPanOffsetY && ((o.y = 0), o.normalize()),
        n.copy(this.node.position).sub(this.lookAt));
      const l = n.length() * 2 * di(ci(this.fov * 0.5));
      this._targetLookAt
        .sub(t.multiplyScalar(e.x * l))
        .add(o.multiplyScalar(e.y * l));
    }
    _calculateTargetSpringArm(e, t) {
      (e && ((this._targetTheta -= e.x), (this._targetPhi += e.y)),
        t && (this._targetSpringLength = t),
        (this._targetTheta = Ue(
          this._targetTheta,
          this.thetaMin,
          this.thetaMax,
        )),
        (this._targetPhi = Ue(this._targetPhi, this.phiMin, this.phiMax)),
        (this._targetSpringLength = Ue(
          this._targetSpringLength,
          this.distanceMin,
          this.distanceMax,
        )));
    }
    gotoPOI({
      duration: e = -1,
      easing: t = j.Cubic.InOut,
      springLength: o = this._targetSpringLength,
      theta: n = this._targetTheta,
      phi: l = this._targetPhi,
      lookAt: m = this._lookAt,
      fov: h = this.lens.fov,
      smoothing: d = this.smoothing,
      rotateSmoothing: C = d,
    }) {
      ((this._targetFov = h),
        (this._tempSmoothing = d),
        (this._tempRotateSmoothing = C),
        this._targetLookAt.copy(m));
      const x = n % Ie,
        L = (this._spherical.theta = this._spherical.theta % Ie),
        I = [x, x - Ie, x + Ie],
        M = I.map((D) => mi(L - D)),
        A = Math.min(...M);
      ((this._targetTheta = I[M.findIndex((D) => D === A)]),
        (this._targetSpringLength = o),
        (this._targetPhi = l),
        this._calculateTargetSpringArm(),
        e > 0 &&
          ((this.locked = !0),
          (this.enabled = !1),
          this.viewer.killTweensOf(this),
          this.viewer
            .timeline(this)
            .to({ springLength: o, theta: n, phi: l, lookAt: m, fov: h }, e, {
              easing: t,
              onUpdate: () => {
                (this.node.position
                  .setFromSpherical(this._spherical)
                  .add(this._lookAt),
                  this.node.lookAt(this._lookAt));
              },
            })
            .call(() => {
              ((this.locked = !1), (this.enabled = !0));
            })
            .start()));
    }
    update(e) {
      const t = this._tempSmoothing,
        o = this._tempRotateSmoothing;
      ((this._spherical.theta = xe(
        this._spherical.theta,
        this._targetTheta,
        e,
        o,
      )),
        (this._spherical.phi = xe(this._spherical.phi, this._targetPhi, e, o)),
        (this._spherical.radius = xe(
          this._spherical.radius,
          this._targetSpringLength,
          e,
          t,
        )),
        (this.lens.fov = xe(this.lens.fov, this._targetFov, e, t)),
        _t(this._lookAt, this._targetLookAt, e, t),
        this.node.position.setFromSpherical(this._spherical).add(this._lookAt),
        this.node.lookAt(this._lookAt));
    }
  }),
  s(R, "__loc0", new $()),
  s(R, "__loc1", new $()),
  s(R, "__center", new $()),
  s(R, "__preCenter", new $()),
  s(R, "__panDelta", new $()),
  s(R, "__panTarget", new $()),
  s(R, "__rotateDelta", new $()),
  s(R, "__posDelta", new p()),
  s(R, "__xAxis", new p()),
  s(R, "__yAxis", new p()),
  s(R, "__quat", new pt()),
  R);
z([S({ dir: "set" })], O.prototype, "forbidX", 2);
z([S({ dir: "set" })], O.prototype, "forbidY", 2);
z([S({ dir: "set" })], O.prototype, "forbidZ", 2);
z([S({ dir: "set" })], O.prototype, "forbidPanX", 2);
z([S({ dir: "set" })], O.prototype, "forbidPanY", 2);
z([S({ dir: "set" })], O.prototype, "forbitPanOffsetY", 2);
z([S({ dir: "set", step: 0.01 })], O.prototype, "panSpeed", 2);
z([S({ dir: "set", step: 0.01 })], O.prototype, "rotateSpeed", 2);
z([S({ dir: "set", step: 0.01 })], O.prototype, "smoothing", 2);
z([S({ dir: "set", step: 0.01 })], O.prototype, "rotateSmoothing", 2);
z([S({ dir: "set", step: 0.01 })], O.prototype, "phiMin", 2);
z([S({ dir: "set", step: 0.01 })], O.prototype, "phiMax", 2);
z([S({ dir: "set", step: 0.01 })], O.prototype, "thetaMin", 2);
z([S({ dir: "set", step: 0.01 })], O.prototype, "thetaMax", 2);
z([S({ dir: "set", step: 0.01 })], O.prototype, "distanceMin", 2);
z([S({ dir: "set", step: 0.01 })], O.prototype, "distanceMax", 2);
z([S({ step: 0.01 })], O.prototype, "lookAt", 1);
z([S({ step: 0.01 })], O.prototype, "springLength", 1);
z([S({ step: 0.01 })], O.prototype, "theta", 1);
z([S({ step: 0.01 })], O.prototype, "phi", 1);
z([S], O.prototype, "fov", 1);
let ui = O;
var hi = Object.defineProperty,
  Q = (c, r, e, t) => {
    for (var o = void 0, n = c.length - 1, l; n >= 0; n--)
      (l = c[n]) && (o = l(r, e, o) || o);
    return (o && hi(r, e, o), o);
  },
  ie;
const Y =
  ((ie = class extends Ae {
    constructor() {
      super(...arguments);
      s(this, "enablePositionNoise", !0);
      s(this, "positionFrequency", 0.2);
      s(this, "positionAmplitude", 0.5);
      s(this, "positionScale", new p(1, 1, 1));
      s(this, "positionFractalLevel", 3);
      s(this, "enableRotationNoise", !0);
      s(this, "rotationFrequency", 0.2);
      s(this, "rotationAmplitude", 10);
      s(this, "rotationScale", new p(1, 1, 0));
      s(this, "rotationFractalLevel", 3);
      s(this, "_initialPosition", new p());
      s(this, "_initialRotation", new yt());
      s(this, "_time", []);
      s(this, "_fbmNorm", 1 / 0.75);
    }
    rehash() {
      for (let e = 0; e < 6; e++) this._time[e] = De.randFloat(-1e4, 0);
    }
    start() {
      (this._initialPosition.copy(this.node.position),
        this._initialRotation.copy(this.node.rotation),
        this.rehash());
    }
    update(e) {
      const { __normal: t, __rotation: o } = ie;
      if (this.enablePositionNoise) {
        for (let l = 0; l < 3; l++) this._time[l] += this.positionFrequency * e;
        let n = t.set(
          ae.Fbm(this.positionFractalLevel, this._time[0]),
          ae.Fbm(this.positionFractalLevel, this._time[1]),
          ae.Fbm(this.positionFractalLevel, this._time[2]),
        );
        ((n = n.multiply(this.positionScale)),
          n.multiplyScalar(this.positionAmplitude * this._fbmNorm),
          this.node.position.copy(this._initialPosition).add(n));
      }
      if (this.enableRotationNoise) {
        for (let l = 0; l < 3; l++)
          this._time[l + 3] += this.rotationFrequency * e;
        let n = t.set(
          ae.Fbm(this.rotationFractalLevel, this._time[3]),
          ae.Fbm(this.rotationFractalLevel, this._time[4]),
          ae.Fbm(this.rotationFractalLevel, this._time[5]),
        );
        ((n = n.multiply(this.rotationScale)),
          n.multiplyScalar(this.rotationAmplitude * this._fbmNorm),
          o.setFromEuler(this._initialRotation).add(n),
          this.node.rotation.setFromVector3(o));
      }
    }
  }),
  s(ie, "__normal", new p()),
  s(ie, "__rotation", new p()),
  ie);
Q([S({ dir: "position" })], Y.prototype, "enablePositionNoise");
Q([S({ dir: "position" })], Y.prototype, "positionFrequency");
Q([S({ dir: "position" })], Y.prototype, "positionAmplitude");
Q([S({ dir: "position" })], Y.prototype, "positionScale");
Q(
  [S({ dir: "position", min: 0, max: 8, step: 1 })],
  Y.prototype,
  "positionFractalLevel",
);
Q([S({ dir: "rotation" })], Y.prototype, "enableRotationNoise");
Q([S({ dir: "rotation" })], Y.prototype, "rotationFrequency");
Q([S({ dir: "rotation" })], Y.prototype, "rotationAmplitude");
Q([S({ dir: "rotation" })], Y.prototype, "rotationScale");
Q(
  [S({ dir: "rotation", min: 0, max: 8, step: 1 })],
  Y.prototype,
  "rotationFractalLevel",
);
let vi = Y;
var pi = Object.defineProperty,
  gi = Object.getOwnPropertyDescriptor,
  nt = (c, r, e, t) => {
    for (var o = gi(r, e), n = c.length - 1, l; n >= 0; n--)
      (l = c[n]) && (o = l(r, e, o) || o);
    return (o && pi(r, e, o), o);
  };
class qe extends Ae {
  async start() {
    (await this._init(),
      await this._preparePlugins(),
      await this._prepareScene(),
      await this._enterScene());
  }
  async _init() {
    const r = this.viewer;
    (r.addLoader(Lt),
      await Promise.all([
        T(r, { url: "environments/env_home.hdr" }, "env_home"),
        T(r, { url: "models/sm_scene01.glb" }, "sm_floor"),
        T(
          r,
          { url: "textures/lm_inside_floor.jpg", flipY: !1, channel: 0 },
          "lm_inside_floor",
        ),
        T(
          r,
          { url: "textures/lm_floor.jpg", flipY: !1, channel: 0 },
          "lm_floor",
        ),
        T(r, { url: "textures/ao_floor.jpg", channel: 1 }, "ao_floor"),
        T(
          r,
          {
            url: "textures/t_floor_roughness.jpg",
            colorSpace: Te,
            wrapS: K,
            wrapT: K,
            anisotropy: 4,
          },
          "t_floor_roughness",
        ),
      ]));
  }
  _preparePlugins() {
    const r = this.viewer;
    ((i.boxProjection = r.addPlugin(St, {
      boxMin: new p(-2.5, 0, -4.3),
      boxMax: new p(2.5, 2.5, 2.5),
    })),
      (i.environment = r.addPlugin(
        new $e({ layer: F.ENV_REFLECT, envMap: y("env_home") }),
      )));
    const e = r.addPlugin(wt, { enable: !0 }),
      t = r.addPlugin(Pt, { enable: !1 }),
      o = r.addPlugin(
        new Ct({
          mipmapBlur: !0,
          luminanceThreshold: 0.5,
          luminanceSmoothing: 1.3,
        }),
      );
    ((i.bloom = o),
      r.addPlugin(
        new xt({
          onDecline: (n) => {
            n.factor < 0.21
              ? (o.enable = !1)
              : n.factor < 0.31
                ? (i.environment.quality = $e.Quality.LOW)
                : n.factor < 0.41
                  ? (t.enable = !1)
                  : n.factor < 0.51 && ((e.enable = !1), (t.enable = !0));
          },
        }),
      ));
  }
  async _prepareScene() {
    const r = this.viewer;
    (N.registerScene("HomeScene", new Gt(r, re.HomeScene)),
      N.registerScene("HomeCity", new Re(r, re.HomeCity)),
      N.registerScene("AIDriveScene", new Kt(r, re.AIDriveScene)),
      N.registerScene("AIDriveRadar", new We(r, re.AIDriveRadar)),
      N.registerScene("InteriorScene", new oi(r, re.InteriorScene)),
      N.registerScene("InteriorDetail", new Ve(r, re.InteriorDetail)),
      await N.prepareScene());
  }
  _enterScene() {
    const r = this.viewer;
    ((i.freeCamera = r.addNode(ui, {
      springLength: 12,
      phi: Math.PI / 2,
      lookAt: new p(0, 0.8, 0),
      fov: 33.4,
      forbidZ: !0,
    })),
      (i.motion = r.addNode(vi, {
        rotationAmplitude: 0,
        positionAmplitude: 0,
      })),
      (i.freeCamera.correctPosition = i.motion.node.position),
      (i.freeCamera.correctRotation = i.motion.node.quaternion));
    const e = r.addNode(
      new bt({ layer: F.PLANE_REFLECT, normal: new p(0, 1, 0), mipmap: !0 }),
    );
    ((i.sharedUnifoms.reflectMap.value = e.getRenderTarget().texture),
      (i.sharedUnifoms.textureMatrix.value = e.getTextureMatrix()));
    const t = new ne();
    ((t.name = "mFloor"),
      (t.defines.ROUGHNESSMAP_WORLDPOS_SCALE = 0.2),
      (t.defines.USE_LIGHTMAP_BLENDING = !0),
      (t.aoMap = y("ao_floor")),
      (t.lightMap = y("lm_floor")),
      (t.roughnessMap = y("t_floor_roughness")),
      (t.lightMap2 = y("lm_inside_floor")),
      (t.transparent = !0),
      (t.depthWrite = !1),
      (t.color = new P(0)),
      (t.lightMapBlending = 0),
      (t.reflectIntensity = 0),
      (t.uniforms.reflectMap = i.sharedUnifoms.reflectMap),
      (t.uniforms.textureMatrix = i.sharedUnifoms.textureMatrix),
      (t.uniforms.uDistance = i.sharedUnifoms.uDistance));
    const o = y("sm_floor"),
      n = _.findChild(o, "scene").geometry;
    ((i.sharedFloor = r.addNode(new Ge(n, t), { position: new p(0, 0.01, 0) })),
      r.addNode(ri));
    const l = r.dpr;
    ((i.sharedUnifoms.uVmin.value =
      Math.min(window.innerWidth, window.innerHeight) * l),
      r.on(
        G.RESIZE,
        (m, h) => {
          i.sharedUnifoms.uVmin.value = Math.min(m, h) * l;
        },
        this,
      ));
  }
  onDestroy() {
    N.clear();
  }
  update(r) {
    (N.updateScene(r), (i.sharedUnifoms.uTime.value += r));
  }
  get state() {
    return v.state;
  }
  set state(r) {
    v.state = r;
  }
  get runningScene() {
    return N.activeSceneName;
  }
  set runningScene(r) {
    N.runScene(r);
  }
}
nt([S({ value: w })], qe.prototype, "state");
nt(
  [
    S({
      value: {
        HomeScene: "HomeScene",
        HomeCity: "HomeCity",
        AIDriveScene: "AIDriveScene",
        AIDriveRadar: "AIDriveRadar",
        InteriorScene: "InteriorScene",
        InteriorDetail: "InteriorDetail",
      },
    }),
  ],
  qe.prototype,
  "runningScene",
);
function _i() {
  const c = it(null);
  return (
    he(() => {
      const r = new _({
        root: document.getElementById("app"),
        canvas: c.current,
        camera: { position: new p(0, 2, 0) },
        orientation: Mt.LANDSCAPE,
        targetFrameRate: 60,
        fixedFrameTime: !0,
        dracoPath: we.dracoPath,
        loader: {
          onProgress: (e, t, o) => (i.loading = Math.max(i.loading, t / o)),
          onLoad: () => (i.loading = 1),
        },
      });
      return (
        (r.renderer.sortObjects = !1),
        r.addNode(qe),
        () => {
          r.destroy();
        }
      );
    }, []),
    a("aside", {
      className: "webgl-wrapper",
      children: a("canvas", {
        ref: c,
        className: "webgl-canvas",
        children: "No Canvas!",
      }),
    })
  );
}
function E() {
  return oe.isMobile && !oe.isTablet;
}
function yi() {
  return (
    i.showButton &&
    a("div", {
      className: "Logo-container",
      onClick: () => {
        console.log("预约试驾功能已禁用");
      },
      children: a("img", {
        className: "Logo-img",
        src: Oe("icons/Logo.png"),
        alt: "",
        style: { height: E() ? "6vmin" : "" },
      }),
    })
  );
}
function Si() {
  return null;
}
const et = "assets/c16-KtZzmDFQ.svg",
  U = ["3.2vmin", "2vmin", "1.8vmin"],
  k = ["4vmin", "3vmin", "2.5vmin"],
  g = "0.2vmin",
  V = 300;
function wi() {
  const [c, r] = q(w.Loading),
    [e, t] = q(!1);
  return (
    he(() => {
      const o = (l) => {
          r(l);
        },
        n = (l) => {
          t(l);
        };
      return (
        v.on(b.STATE_CHANGED, o),
        v.on(b.PRESSED, n),
        () => {
          (v.off(b.STATE_CHANGED, o), v.off(b.PRESSED, n));
        }
      );
    }, []),
    a(Pe, {
      children: [

      ],
    })
  );
}
const Pi = [w.State1, w.State2, w.State3],
  Ci = ["*", "*", "*"];
function xi() {
  const [c, r] = q(w.Loading),
    [e, t] = q(!1);
  return (
    he(() => {
      const o = (l) => {
          r(l);
        },
        n = (l) => {
          t(l);
        };
      return (
        v.on(b.STATE_CHANGED, o),
        v.on(b.PRESSED, n),
        () => {
          (v.off(b.STATE_CHANGED, o), v.off(b.PRESSED, n));
        }
      );
    }, []),
    a(Pe, {
      children: a("div", {
        className: "StateTable-container",
        style: { 
          opacity: e ? 0 : 1, 
          transform: e ? "translateX(100%)" : "", 
          position: "absolute",
          right: "2vmin",
          top: "50%",
          translate: "0 -50%",
        },
        children: a(Se, {
          mode: "popLayout",
          children:
            c != w.BeginAnim &&
            c != w.Loading &&
            a(J.div, {
              className: "StateTable-content",
              initial: { x: 10, opacity: 0 },
              animate: { x: 0, opacity: 1 },
              exit: { x: -10, opacity: 0 },
              transition: { duration: 0.5 },
              children: [
                Pi.map((o, n) =>
                  a(
                    "div",
                    {
                      className: "item",
                      style: { transform: o == c ? "scale(1.1)" : "" },
                      onClick: () => {
                        !v.presssed && o != c && (v.state = o);
                        window.dispatchInteractionEvent && window.dispatchInteractionEvent('divClick', { containerIndex: 0, divIndex: n, className: 'item' });
                      },
                      children: [
                        a("div", {
                          className: "item-text",
                          style: {
                            opacity: o == c ? 1 : "",
                            fontSize: E() ? "3.2vmin" : "",
                            marginRight: E() ? "4vmin" : "",
                          },
                          children: Ci[n],
                        }),
                        a("div", {
                          className: "item-Line",
                          style: { height: E() ? "12vmin" : "" },
                        }),
                      ],
                    },
                    n,
                  ),
                ),
              ],
            }),
        }),
      }),
    })
  );
}
const bi = [0, 1, 2, 3, 4, 5];
function Mi() {
  const [c, r] = q(w.Loading),
    [e, t] = q(!1),
    [o, n] = q(0);
  return (
    he(() => {
      const l = (d) => {
          r(d);
        },
        m = (d) => {
          t(d);
        },
        h = (d) => {
          n(d);
        };
      return (
        v.on(b.STATE_CHANGED, l),
        v.on(b.PRESSED, m),
        v.on(b.COLORBODY_CHANGED, h),
        () => {
          (v.off(b.STATE_CHANGED, l),
            v.off(b.PRESSED, m),
            v.off(b.COLORBODY_CHANGED, h));
        }
      );
    }, []),
    a(Pe, {
      children: a("div", {
        style: { opacity: e ? 0 : 1, transition: "0.5s all 0.5s" },
        children: a(Se, {
          mode: "popLayout",
          children:
            (c == w.State2 || c == w.State1) &&
            a(J.div, {
              className: "StateTable-container",
              initial: { y: 30, opacity: 0 },
              animate: { y: 0, opacity: 1 },
              exit: { y: 30, opacity: 0 },
              transition: { duration: 0.2 },
              style: {
                left: 0,
                fontSize: "3.2vmin",
                marginRight: "4vmin",
                height: "100%",
                color: "white",
                width: "40px",
                top: 0,
              },
              children: a("div", {
                className: "StateTable-content",
                children: [
                  bi.map((l, m) =>
                    a("div", {
                      className: "Bar",
                      onClick: () => {
                        !v.presssed && l != o && (v.colorBodyIndex = l);
                        window.dispatchInteractionEvent && window.dispatchInteractionEvent('divClick', { containerIndex: 1, divIndex: m, className: 'Bar' });
                      },
                      children: [
                        a("div", {
                          className: "Bar-Text",
                          children: "*",
                        }),
                        a("div", {
                          className: "Bar-Line",
                        }),
                      ],
                    }),
                  ),
                ],
              }),
            }),
        }),
      }),
    })
  );
}
const Ii = [0, 1, 2];
function Ti() {
  const [c, r] = q(w.Loading),
    [e, t] = q(!1),
    [o, n] = q(0);
  return (
    he(() => {
      const l = (d) => {
          r(d);
        },
        m = (d) => {
          t(d);
        },
        h = (d) => {
          n(d);
        };
      return (
        v.on(b.STATE_CHANGED, l),
        v.on(b.PRESSED, m),
        v.on(b.COLORINTERIOR_CHANGED, h),
        () => {
          (v.off(b.STATE_CHANGED, l),
            v.off(b.PRESSED, m),
            v.off(b.COLORINTERIOR_CHANGED, h));
        }
      );
    }, []),
    a(Pe, {
      children: a("div", {
        style: { opacity: e ? 0 : 1, transition: "0.2s all 0.5s" },
        children: a(Se, {
          mode: "popLayout",
          children:
            c == w.State3 &&
            a(J.div, {
              className: "StateTable-container",
              initial: { y: 30, opacity: 0 },
              animate: { y: 0, opacity: 1 },
              exit: { y: 30, opacity: 0 },
              transition: { duration: 0.2 },
              style: {
                left: 0,
                fontSize: "3.2vmin",
                marginRight: "4vmin",
                height: "100%",
                color: "white",
                width: "40px",
                top: 0,
              },
              children: a("div", {
                className: "StateTable-content",
                children: [
                  Ii.map((l, m) =>
                    a("div", {
                      className: "Bar",
                      onClick: () => {
                        !v.presssed && l != o && (v.colorInteriorIndex = l);
                        window.dispatchInteractionEvent && window.dispatchInteractionEvent('divClick', { containerIndex: 2, divIndex: m, className: 'Bar' });
                      },
                      children: [
                        a("div", {
                          className: "Bar-Text",
                          children: "*",
                        }),
                        a("div", {
                          className: "Bar-Line",
                        }),
                      ],
                    }),
                  ),
                ],
              }),
            }),
        }),
      }),
    })
  );
}
function Li() {
  const c = it(null),
    [r, e] = q(0);
  return (
    he(() => {
      let t = 0,
        o = !1;
      const n = setInterval(() => {
          ((t = De.damp(t, i.loading, 1, 1)),
            t >= 0.99 && (t = 1),
            o &&
              t >= 1 &&
              ((o = !1),
              (c.current.style.transition =
                "500ms opacity 600ms cubic-bezier(0,0,.58,1)"),
              (c.current.style.opacity = "0"),
              setTimeout(() => (c.current.style.display = "none"), 1200)),
            e(t));
        }),
        l = () => {
          o = !0;
        };
      return (
        v.on(b.PRELOADED, l),
        () => {
          (clearInterval(n), v.off(b.PRELOADED, l));
        }
      );
    }, []),
    a("aside", {
      id: "preloader",
      ref: c,
      children: [
        a("p", { id: "version", children: we.VERSION }),
        a("div", {
          className: "svg-content",
          style: { ...(E() ? { height: "20vmin", width: "20vmin" } : {}) },
          children: [
            a("svg", {
              className: "preloader-svg-path",
              xmlns: "http://www.w3.org/2000/svg",
              xmlnsXlink: "http://www.w3.org/1999/xlink",
              width: "360",
              height: "360",
              viewBox: "0 0 360 360",
              fill: "none",
              children: a("path", {
                stroke: "rgba(255, 255, 255, 1)",
                "stroke-width": "43",
                style: { strokeDashoffset: 738.05 * (1 - r) },
                d: "M117 107.5C117 107.5 117 169.7 117 210.5L142.5 225L142.5 312.5L231 312.5L231 224L250.5 212.5L251.5 141L186 107.5L186 281",
              }),
            }),
            a("svg", {
              className: "preloader-svg-mask",
              xmlns: "http://www.w3.org/2000/svg",
              xmlnsXlink: "http://www.w3.org/1999/xlink",
              width: "360",
              height: "360",
              viewBox: "0 0 360 360",
              fill: "none",
              children: a("path", {
                "fill-rule": "evenodd",
                fill: "#000",
                d: "M360 0L0 0L0 360L360 360L360 0ZM170.612 160.843C170.609 167.068 170.606 173.294 170.606 179.519C170.606 185.721 170.608 191.923 170.611 198.125C170.619 219.206 170.627 240.284 170.538 261.344C170.529 264.523 171.212 266.547 174.244 268.242C176.332 269.405 177.995 270.39 179.36 271.199C182.545 273.087 184.11 274.014 185.667 274C187.863 273.981 190.044 272.088 196.742 268.377C199.822 266.672 200.871 264.707 200.871 261.2Q200.785 198.846 200.871 136.483C200.871 134.835 201.555 133.188 201.921 131.54C203.557 132.08 205.319 132.234 206.734 133.043C215.485 138.082 224.1 143.342 232.88 148.323C233.013 148.386 233.144 148.453 233.272 148.526C233.401 148.598 233.526 148.676 233.649 148.758C233.771 148.841 233.89 148.928 234.006 149.019C234.122 149.111 234.233 149.207 234.341 149.307C234.449 149.407 234.553 149.511 234.653 149.62C234.753 149.729 234.849 149.841 234.94 149.956C235.031 150.073 235.118 150.192 235.2 150.315C235.282 150.437 235.359 150.563 235.431 150.691C235.503 150.82 235.57 150.951 235.632 151.085C235.694 151.219 235.751 151.355 235.802 151.493C235.854 151.632 235.9 151.772 235.94 151.914C235.98 152.055 236.015 152.198 236.045 152.344C236.074 152.488 236.098 152.634 236.116 152.78C236.134 152.927 236.146 153.073 236.153 153.22C236.16 153.368 236.161 153.515 236.156 153.663C236.151 153.81 236.14 153.957 236.124 154.104C235.989 167.419 235.97 180.743 236.124 194.068C236.137 194.215 236.145 194.362 236.147 194.511C236.148 194.658 236.144 194.803 236.134 194.944C236.125 195.092 236.109 195.239 236.088 195.387C236.067 195.535 236.04 195.679 236.008 195.821C235.976 195.962 235.937 196.103 235.894 196.245C235.85 196.386 235.801 196.524 235.747 196.659C235.693 196.794 235.633 196.929 235.568 197.064C235.503 197.199 235.433 197.327 235.358 197.449C235.283 197.577 235.204 197.703 235.119 197.825C235.034 197.941 234.945 198.056 234.85 198.172C234.757 198.287 234.659 198.396 234.557 198.499C234.454 198.601 234.347 198.702 234.237 198.798C234.127 198.9 234.013 198.994 233.895 199.077C233.778 199.167 233.657 199.251 233.532 199.328C233.408 199.411 233.282 199.485 233.152 199.549C233.022 199.62 232.889 199.684 232.755 199.742C228.204 202.105 223.789 204.7 219.508 207.527C217.891 208.625 216.033 210.851 216.033 212.594C215.781 224.091 215.83 235.574 215.878 247.054L215.878 247.056C215.885 248.736 215.892 250.415 215.898 252.095C215.908 255.592 217.458 256.276 220.481 254.484C234.542 246.122 248.632 237.846 262.751 229.657C265.379 228.173 265.976 226.169 265.976 223.356C265.873 194.453 265.873 165.549 265.976 136.646C265.99 136.481 265.998 136.316 266 136.149C266.001 135.984 265.996 135.818 265.985 135.652C265.975 135.486 265.957 135.322 265.933 135.158C265.91 134.994 265.881 134.83 265.845 134.668C265.809 134.507 265.767 134.346 265.719 134.187C265.671 134.028 265.617 133.871 265.557 133.717C265.497 133.562 265.431 133.41 265.36 133.26C265.288 133.11 265.211 132.964 265.129 132.82C265.046 132.676 264.958 132.535 264.865 132.399C264.771 132.261 264.673 132.128 264.569 131.999C264.466 131.869 264.358 131.743 264.245 131.622C264.132 131.501 264.015 131.384 263.893 131.27C263.771 131.158 263.645 131.051 263.515 130.948C263.385 130.845 263.251 130.747 263.114 130.654C262.976 130.561 262.836 130.474 262.691 130.392C262.547 130.31 262.4 130.233 262.25 130.163C240.43 117.317 218.61 104.471 196.79 91.6251C195.714 90.9898 194.748 90.4116 193.874 89.889C189.217 87.1024 187.197 85.8935 185.254 86.0073C182.98 86.1405 180.812 88.0851 174.649 91.4324C171.491 93.1473 170.529 95.1128 170.529 98.5907C170.632 119.341 170.622 140.092 170.612 160.843ZM134.842 115.509C123.935 121.261 114.164 126.724 105 133.275L105.038 226.728C120.364 235.997 135.641 245.332 151.044 254.485C154.095 256.296 155.222 254.716 155.222 251.402C155.18 246.336 155.183 241.271 155.186 236.205C155.191 228.439 155.196 220.674 155.039 212.913C155.009 212.655 154.967 212.402 154.912 212.152C154.856 211.901 154.788 211.651 154.708 211.4C154.627 211.156 154.534 210.918 154.429 210.687C154.324 210.45 154.207 210.218 154.079 209.993C153.951 209.769 153.811 209.55 153.661 209.338C153.51 209.133 153.35 208.934 153.179 208.741C153.008 208.548 152.828 208.362 152.639 208.182C152.449 208.009 152.251 207.845 152.045 207.691C147.957 204.865 143.692 202.334 139.251 200.099C136.084 198.471 135.17 196.264 135.17 192.806C135.268 175.75 135.253 158.695 135.238 141.639C135.233 135.438 135.227 129.236 135.227 123.034C135.227 121.011 135.025 118.997 134.842 115.509Z",
              }),
            }),
          ],
        }),
        a("p", {
          className: "LoadingNumber",
          style: { ...(E() ? { fontSize: "3vmin", marginTop: "-2vmin" } : {}) },
          children: [Math.floor(r * 100), "%"],
        }),
      ],
    })
  );
}
function Ei() {
  return a(Pe, {
    children: [
      a(Li, {}),
      a(_i, {}),
      a(yi, {}),
      a(Si, {}),
      a(wi, {}),
      a(xi, {}),
      a(Mi, {}),
      a(Ti, {}),
    ],
  });
}
It(a(Ei, {}), document.getElementById("app"));
